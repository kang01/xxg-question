package org.fwoxford.service;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.QuartzTask;
import org.fwoxford.domain.Question;
import org.fwoxford.domain.ReplyRecord;
import org.fwoxford.domain.SendRecord;
import org.fwoxford.repository.QuartzTaskRepository;
import org.fwoxford.repository.QuestionRepository;
import org.fwoxford.repository.ReplyRecordRepository;
import org.fwoxford.repository.SendRecordRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by gengluying on 2018/6/26.
 * 问题在有效期内尚未回复，回复已过期。
 * 如果问题无回复，问题为已过期，如果有部分回复，则只更改发送的状态，回复的状态
 */
public class ScheduleTaskReply implements Job {
    private static final Logger LOGGER =  LoggerFactory.getLogger(ScheduleTaskReply.class);
    @Autowired
    QuartzTaskRepository quartzTaskRepository;
    @Autowired
    SendRecordRepository sendRecordRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ReplyRecordRepository replyRecordRepository;
    @Autowired
    SendRecordService sendRecordService;

    /**
     * 若需要停止此任务,需要调用QuartzManager中的 removeJob()方法,参数: jobName,jobGroupName,triggerName,triggerGroupName
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String,Object> paramMap =  jobExecutionContext.getJobDetail().getJobDataMap();
        //获取任务ID，任务已完成
        Long taskId = paramMap.get("id")!=null ? Long.valueOf(paramMap.get("id").toString()):null;
        QuartzTask quartzTask = quartzTaskRepository.findOne(taskId);
        quartzTask.setStatus(Constants.TASK_STATUS_FINISHED);
        quartzTaskRepository.save(quartzTask);
        //获取发送ID
        Long sendId = paramMap.get("businessId")!=null ? Long.valueOf(paramMap.get("businessId").toString()):null;
        SendRecord sendRecord = sendRecordRepository.findOne(sendId);
        //判断是否已回复完成,如果未完成,则状态未已过期
        ReplyRecord replyRecord = replyRecordRepository.findBySendRecordId(sendId);
        //判断发送记录是否过期 如果到了过期时间，看发送过多少次，如果没到5次，重新发送
        if(replyRecord ==null||!replyRecord.getStatus().equals(Constants.QUESTION_REPLY_FINISHED)){
            sendRecord.status(Constants.QUESTION_SEND_OVERDUE);
            sendRecordRepository.save(sendRecord);
        }
        //判断回复记录是否过期
        if(replyRecord!=null && !replyRecord.getStatus().equals(Constants.QUESTION_REPLY_FINISHED)){
            replyRecord.status(Constants.QUESTION_REPLY_OVERDUE);
            replyRecordRepository.save(replyRecord);
        }
        //获取问题ID 判断问题是否过期
        Long questionId = sendRecord.getQuestionId();
        //看发送过多少次，如果没到5次，重新发送
        Long countOfSendTimes = sendRecordRepository.countByQuestionIdAndStatusNot(questionId,Constants.INVALID);
        if(countOfSendTimes!=null && countOfSendTimes.intValue()<5){
            sendRecordService.saveSendRecordForReSend(sendId);
            LOGGER.info("问题在有效期内尚未回复，回复已过期并重新发送!");
        }else{
            Question question = questionRepository.findOne(questionId);
            //判断这个问题下的发送记录是否都是已过期,如果是,问题则为已过期
            //已过期记录
            Long countOfUnCount = sendRecordRepository.countByQuestionIdAndStatus(questionId,Constants.QUESTION_SEND_OVERDUE);
            //已回复记录
            Long countOfReplyed = sendRecordRepository.countByQuestionIdAndStatus(questionId,Constants.QUESTION_SEND_REPLIED);
            if(countOfReplyed == 0 && countOfUnCount!=0){
                question.status(Constants.QUESTION_OVERDUE);
                questionRepository.save(question);
            }else if(countOfReplyed!=0){
                question.status(Constants.QUESTION_REPLY);
                questionRepository.save(question);
            }
            LOGGER.info("问题在有效期内尚未回复，回复已过期!");
        }
    }
}
