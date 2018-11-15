package org.fwoxford.service;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.AuthorizationRecord;
import org.fwoxford.domain.QuartzTask;
import org.fwoxford.repository.AuthorizationRecordRepository;
import org.fwoxford.repository.QuartzTaskRepository;
import org.fwoxford.service.dto.EmailMessage;
import org.fwoxford.service.dto.MessagerDTO;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by gengluying on 2018/6/26.
 * 任务到期提醒
 * 如果授权码还有一小时到期，则邮件提醒授权码已经快到期
 */
public class ScheduleTaskMessage implements Job {
    private static final Logger LOGGER =  LoggerFactory.getLogger(ScheduleTaskMessage.class);
    @Autowired
    MailService mailService;
    @Autowired
    QuartzTaskRepository quartzTaskRepository;
    @Autowired
    AuthorizationRecordRepository authorizationRecordRepository;

    /**
     * 若需要停止此任务,需要调用QuartzManager中的 removeJob()方法,参数: jobName,jobGroupName,triggerName,triggerGroupName
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("授权码还有一小时到期提醒开始！");
        Map<String , Object> paramMap = jobExecutionContext.getJobDetail().getJobDataMap();
        //获取任务ID，任务已完成
        Long taskId = paramMap.get("id")!=null ? Long.valueOf(paramMap.get("id").toString()):null;
        QuartzTask quartzTask = quartzTaskRepository.findOne(taskId);
        quartzTask.setStatus(Constants.TASK_STATUS_FINISHED);
        quartzTaskRepository.save(quartzTask);
        //获取授权码ID
        Object authIdObj = paramMap.get("businessId");
        if(authIdObj == null){
            throw new BankServiceException("授权ID不存在！");
        }
        Long authId = Long.valueOf(authIdObj.toString());
        AuthorizationRecord authorizationRecord = authorizationRecordRepository.findOne(authId);
        if(authorizationRecord == null){
            throw new BankServiceException("授权记录查找失败！");
        }
        EmailMessage emailMessage = new EmailMessage();
        MessagerDTO messagerDTO = new MessagerDTO();
        messagerDTO.setFromUser(Constants.EMAIL_SENDER);
        messagerDTO.setToUser(authorizationRecord.getStrangerEmail());
        emailMessage.setAuthorizationCode(authorizationRecord.getAuthorizationCode());
        emailMessage.setStrangerName(authorizationRecord.getStrangerName());
        emailMessage.setHttpUrl(authorizationRecord.getHttpUrl());
        mailService.sendMessageMail(emailMessage, "授权码到期提醒", "messageNotice.ftl",messagerDTO);
        LOGGER.info("授权码还有一小时到期提醒成功！");
    }
}
