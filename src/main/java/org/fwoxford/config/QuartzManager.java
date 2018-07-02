package org.fwoxford.config;

import org.fwoxford.domain.QuartzTask;
import org.fwoxford.repository.QuartzTaskRepository;
import org.fwoxford.service.dto.QuartzTaskDTO;
import org.fwoxford.service.mapper.QuartzTaskMapper;
import org.fwoxford.web.rest.util.BankUtil;
import org.quartz.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by gengluying on 2018/6/28.
 */
@Service
public class QuartzManager {
    private static final org.slf4j.Logger LOGGER =  LoggerFactory.getLogger(QuartzManager.class);
    @Resource(name = "scheduler")
    private Scheduler scheduler;

    @Autowired
    QuartzTaskRepository quartzTaskRepository;
    @Autowired
    QuartzTaskMapper quartzTaskMapper;
    public QuartzManager(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    /**
     * 添加一个定时任务
     *
     * @param jobName           任务名
     * @param jobGroupName      任务组名
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @param jobClass          任务
     * @param cron              时间设置，参考quartz说明文档
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String cron, QuartzTaskDTO quartzTaskDTO) {
        try {
            //根据cron 获取下一次的执行时间
            CronExpression cronExpression = new CronExpression(cron);
            //开始时间
            Date startDate = new Date();
            Date nextValidTime = cronExpression.getNextValidTimeAfter(startDate);
            if(nextValidTime==null || (nextValidTime!=null &&nextValidTime.before(startDate))){
                LOGGER.info(jobName+"任务已结束！");
                quartzTaskDTO.setEnableStatus(Constants.TASK_ENABLE_STATUS_NO);
                quartzTaskDTO.setStatus(Constants.INVALID);
                quartzTaskRepository.save(quartzTaskMapper.toEntity(quartzTaskDTO));
                return;
            }
            // 任务名，任务组，任务执行类
//            MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
//            jobDetail.setConcurrent(false);//下一个任务在上一个任务结束后再执行
//            jobDetail.setName(jobName);
//            jobDetail.setGroup(jobGroupName);
//            jobDetail.setTargetObject(jobClass);
//            jobDetail.setTargetMethod("taskDelayNotice");
//            JobDetail job = jobDetail.getObject();
            JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
//            job = JobBuilder.newJob(jobClass).build();

            // 任务参数
            job.getJobDataMap().putAll(BankUtil.convertToMap(quartzTaskDTO));
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(job, trigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改一个任务的触发时间
     *
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @param cron              时间设置，参考quartz说明文档
     */
    public void modifyJobTime(String triggerName, String triggerGroupName, String cron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 移除一个任务
     *
     * @param jobName           任务名
     * @param jobGroupName      任务组名
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     */
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取任务是否存在
     *
     * STATE_BLOCKED 4 阻塞
     * STATE_COMPLETE 2 完成
     * STATE_ERROR 3 错误
     * STATE_NONE -1 不存在
     * STATE_NORMAL 0 正常
     * STATE_PAUSED 1 暂停
     *
     */
    public  Boolean notExists(String triggerName, String triggerGroupName) {
        try {
            return scheduler.getTriggerState(TriggerKey.triggerKey(triggerName, triggerGroupName)) == Trigger.TriggerState.NONE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createJob(List<QuartzTaskDTO> quartzTaskDTOS) {
        for(QuartzTaskDTO quartzTask :quartzTaskDTOS){
            try {
            this.addJob(quartzTask.getJobName(),
                    quartzTask.getJobGroup(),
                    quartzTask.getTriggerName(),
                    quartzTask.getTriggerGroup(),
                    Class.forName(quartzTask.getClassName()),
                    quartzTask.getTriggerTime(),
                    quartzTask );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
