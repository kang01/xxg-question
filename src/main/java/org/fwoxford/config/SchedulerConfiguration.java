package org.fwoxford.config;

import org.fwoxford.service.QuartzTaskService;
import org.fwoxford.service.dto.QuartzTaskDTO;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by gengluying on 2018/6/25.
 * 初始化JOB
 */
@Configuration
public class SchedulerConfiguration {
    private final Logger log = LoggerFactory.getLogger(SchedulerConfiguration.class);
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    QuartzTaskService quartzTaskService;

    @Autowired
    private QuartzManager quartzManager;

    @Bean
    public StdSchedulerFactory stdSchedulerFactory() {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        //是哪个定时任务配置在执行，可以看到，因为在前面我们将描述设置为了配置类的toString结果
        //获取数据库中的定时任务
        List<QuartzTaskDTO> quartzTaskDTOS = quartzTaskService.findAllValidTask();
        for (QuartzTaskDTO quartzTaskDTO : quartzTaskDTOS) {

            try {
                quartzManager.addJob(quartzTaskDTO.getJobName(),
                    quartzTaskDTO.getJobGroup(),
                    quartzTaskDTO.getTriggerName(),
                    quartzTaskDTO.getTriggerGroup(),
                    Class.forName(quartzTaskDTO.getClassName()),
                    quartzTaskDTO.getTriggerTime(),quartzTaskDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("QuartzJobListener 启动了");

        return stdSchedulerFactory;
    }

}
