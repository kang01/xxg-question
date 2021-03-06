package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.AuthorizationRecord;
import org.fwoxford.domain.QuartzTask;
import org.fwoxford.domain.Question;
import org.fwoxford.repository.*;
import org.fwoxford.service.MailService;
import org.fwoxford.service.QuartzTaskService;
import org.fwoxford.service.SendRecordService;
import org.fwoxford.domain.SendRecord;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.mapper.QuartzTaskMapper;
import org.fwoxford.service.mapper.SendRecordMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Service Implementation for managing SendRecord.
 */
@Service
@Transactional
public class SendRecordServiceImpl implements SendRecordService {

    private final Logger log = LoggerFactory.getLogger(SendRecordServiceImpl.class);

    private final SendRecordRepository sendRecordRepository;

    private final SendRecordMapper sendRecordMapper;
    @Autowired
    MailService mailService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionItemDetailsRepository questionItemDetailsRepository;
    @Autowired
    AuthorizationRecordRepository authorizationRecordRepository;
    @Autowired
    QuartzTaskService quartzTaskService;
    @Autowired
    QuartzTaskRepository quartzTaskRepository;
    @Autowired
    QuartzTaskMapper quartzTaskMapper;

    public SendRecordServiceImpl(SendRecordRepository sendRecordRepository, SendRecordMapper sendRecordMapper) {
        this.sendRecordRepository = sendRecordRepository;
        this.sendRecordMapper = sendRecordMapper;
    }

    /**
     * Save a sendRecord.
     *
     * @param sendRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SendRecordDTO save(SendRecordDTO sendRecordDTO) {
        log.debug("Request to save SendRecord : {}", sendRecordDTO);
        SendRecord sendRecord = sendRecordMapper.sendRecordDTOToSendRecord(sendRecordDTO);
        sendRecord = sendRecordRepository.save(sendRecord);
        return sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);
    }

    /**
     * Get all the sendRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SendRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SendRecords");
        return sendRecordRepository.findAll(pageable)
            .map(sendRecordMapper::sendRecordToSendRecordDTO);
    }

    /**
     * Get one sendRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SendRecordDTO findOne(Long id) {
        log.debug("Request to get SendRecord : {}", id);
        SendRecord sendRecord = sendRecordRepository.findOne(id);
        return sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);
    }

    /**
     * Delete the sendRecord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SendRecord : {}", id);
        sendRecordRepository.delete(id);
    }


    @Override
    public List<SendRecordDTO> sendEmailRecordToStranger(Question question, List<AuthorizationRecord> authorizationRecordsForSave) {
        //一个问题只能发送给一个人一次（不在过期状态）
        List<SendRecord> sendRecordsOldList = sendRecordRepository.findByQuestionIdAndStatusNot(question.getId(),Constants.QUESTION_SEND_OVERDUE);
        List<SendRecord> sendRecords = new ArrayList<>();
        List<SendRecordDTO> sendRecordDTOS = new ArrayList<>();
        for(AuthorizationRecord authorizationRecord : authorizationRecordsForSave){
            SendRecord sendRecordsOld = sendRecordsOldList.stream().filter(s->s.getStrangerEmail().equals(authorizationRecord.getStrangerEmail())).findFirst().orElse(null);
            if(sendRecordsOld!=null){
                continue;
            }
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setAuthor(question.getAuthor());
            emailMessage.setAuthorizationCode(authorizationRecord.getAuthorizationCode());
            emailMessage.setStrangerName(authorizationRecord.getStrangerName());
            emailMessage.setOccurDate(question.getOccurDate());
            emailMessage.setQuestionType(Constants.QUESTION_TYPE_MAP.get(question.getQuestionTypeCode()));
            emailMessage.setProjectCode(question.getProjectCode());
            emailMessage.setProjectName(question.getProjectName());
            emailMessage.setQuestionDescription(question.getQuestionDescription());
            emailMessage.setQuestionSummary(question.getQuestionSummary());
            emailMessage.setHttpUrl(authorizationRecord.getHttpUrl());
            MessagerDTO messagerDTO = new MessagerDTO();
            messagerDTO.setFromUser(Constants.EMAIL_SENDER);
            messagerDTO.setToUser(authorizationRecord.getStrangerEmail());
            mailService.sendMessageMail(emailMessage, question.getQuestionSummary(), "message.ftl",messagerDTO);
            SendRecord sendRecord = new SendRecord();
            sendRecord.questionCode(question.getQuestionCode()).questionId(question.getId())
                .authorizationRecordId(authorizationRecord.getId()).status(Constants.QUESTION_SENT)
                .senderEmail(Constants.EMAIL_SENDER).strangerName(authorizationRecord.getStrangerName()).strangerEmail(authorizationRecord.getStrangerEmail());
            sendRecords.add(sendRecord);
        }
        sendRecordRepository.save(sendRecords);
        sendRecordDTOS = sendRecordMapper.sendRecordsToSendRecordDTOs(sendRecords);
        sendRecordDTOS.forEach(s->{
            AuthorizationRecord authorizationRecord =authorizationRecordsForSave.stream().filter(a->a.getId().equals(s.getAuthorizationRecordId())).findFirst().orElse(null);
            if(authorizationRecord == null){
                throw new BankServiceException("发送记录获取授权信息失败!");
            }
            s.setExpirationTime(authorizationRecord.getExpirationTime());
        });
        return sendRecordDTOS;
    }

    /**
     * 查询某一个问题的发送记录
     * @param questionId
     * @return
     */
    @Override
    public List<SendRecordDTO> findSendRecordByQuestionId(Long questionId) {
        List<SendRecord> sendRecords = sendRecordRepository.findByQuestionId(questionId);
        Question question = questionRepository.findOne(questionId);
        if(question == null){
            throw new BankServiceException("问题不存在!",questionId.toString());
        }
        List<SendRecordDTO> alist = sendRecordMapper.sendRecordsToSendRecordDTOs(sendRecords);
        Long countOfSample = questionItemDetailsRepository.countByQuestionId(questionId);
        alist.forEach(s->{
            s.setProjectName(question.getProjectName());
            s.setSendDate(s.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDate());
            s.setProjectCode(question.getProjectCode());
            s.setProjectId(question.getProject().getId());
            s.setQuestionSummary(question.getQuestionSummary());
            s.setQuestionTypeCode(question.getQuestionTypeCode());
            s.setCountOfSample(countOfSample);
        });
        return alist;
    }

    /**
     * 重发
     * @param id
     * @return
     */
    @Override
    public SendRecordDTO saveSendRecordForReSend(Long id) {
        SendRecord sendRecord = sendRecordRepository.findOne(id);
        if(sendRecord ==null || !sendRecord.getStatus().equals(Constants.QUESTION_SEND_OVERDUE)){
            throw new BankServiceException("上一次发送未过期，无法再次发送！");
        }
        sendRecord.status(Constants.QUESTION_SEND_RESEND);
        sendRecordRepository.save(sendRecord);
        Long questionId =  sendRecord.getQuestionId();
        Long authorizationId = sendRecord.getAuthorizationRecordId();
        //
        Question question = questionRepository.findOne(questionId);
        if(question == null || (question!=null && question.getStatus().equals(Constants.QUESTION_FINISHED))){
            throw new BankServiceException("问题"+sendRecord.getQuestionCode()+"已结束，无法再次发送！");
        }
        //授权加时
        AuthorizationRecord authorizationRecord = authorizationRecordRepository.findOne(authorizationId);
        if(authorizationRecord==null){
            throw new BankServiceException("未查询到"+sendRecord.getStrangerEmail()+"的上一次授权信息！");
        }

        authorizationRecord.setExpirationTime(BankUtil.getExpriationTime());
        authorizationRecordRepository.save(authorizationRecord);

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setAuthor(question.getAuthor());
        emailMessage.setAuthorizationCode(authorizationRecord.getAuthorizationCode());
        emailMessage.setStrangerName(authorizationRecord.getStrangerName());
        emailMessage.setOccurDate(question.getOccurDate());
        emailMessage.setQuestionType(Constants.QUESTION_TYPE_MAP.get(question.getQuestionTypeCode()));
        emailMessage.setProjectCode(question.getProjectCode());
        emailMessage.setProjectName(question.getProjectName());
        emailMessage.setQuestionDescription(question.getQuestionDescription());
        emailMessage.setQuestionSummary(question.getQuestionSummary());
        emailMessage.setHttpUrl(authorizationRecord.getHttpUrl());
        MessagerDTO messagerDTO = new MessagerDTO();
        messagerDTO.setFromUser(Constants.EMAIL_SENDER);
        messagerDTO.setToUser(authorizationRecord.getStrangerEmail());
        mailService.sendMessageMail(emailMessage, question.getQuestionSummary(), "message.ftl",messagerDTO);
        SendRecord sendRecordNew = new SendRecord();
        sendRecordNew.questionCode(question.getQuestionCode()).questionId(question.getId())
            .authorizationRecordId(authorizationRecord.getId()).status(Constants.QUESTION_SENT)
            .senderEmail(Constants.EMAIL_SENDER).strangerName(authorizationRecord.getStrangerName()).strangerEmail(authorizationRecord.getStrangerEmail());
        sendRecordRepository.save(sendRecordNew);
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecordNew);
        sendRecordDTO.setExpirationTime(authorizationRecord.getExpirationTime());
        //重新创造定时任务
        quartzTaskService.createQuartzTaskForDelayCheck(new ArrayList<SendRecordDTO>(){{add(sendRecordDTO);}});
        quartzTaskService.createQuartzTaskForNotice(new ArrayList<SendRecordDTO>(){{add(sendRecordDTO);}});
        //重发后,问题为已提问
        question.status(Constants.QUESTION_ASKED);
        questionRepository.save(question);
        return sendRecordDTO;
    }

    /**
     * 申请加时
     * @param id  发送ID
     * @return
     */
    @Override
    public SendRecordDTO increaseTimeSendRecord(Long id) {
        //根据发送ID获取授权ID,更改授权过期时间
        //找到当前发送记录的定时任务,更改触发时间
        SendRecord sendRecord = sendRecordRepository.findOne(id);
        //什么时候可以申请加时,已发送,回复中可以
        if(sendRecord == null || (sendRecord!=null
                && !sendRecord.getStatus().equals(Constants.QUESTION_SENT)
                && !sendRecord.getStatus().equals(Constants.QUESTION_SEND_REPLY_PENDING)
        )){
            throw new BankServiceException("此次发送不在进行中,不能申请加时！");
        }
        Long authId = sendRecord.getAuthorizationRecordId();
        AuthorizationRecord authorizationRecord = authorizationRecordRepository.findOne(authId);
        if(authorizationRecord == null){
            throw new BankServiceException("授权信息查询失败,不能申请加时！");
        }
        if(authorizationRecord.getApplyTimes().equals(Constants.APPLY_TIMES_MAX)){
            throw new BankServiceException("此次申请加时已达到最大上限("+Constants.APPLY_TIMES_MAX+"次)！");
        }
        ZonedDateTime expriationTime = authorizationRecord.getExpirationTime().plusMinutes(Constants.INCREASE_MINUTES);
        authorizationRecord.expirationTime(expriationTime);
        authorizationRecord.applyTimes(authorizationRecord.getApplyTimes()!=null?authorizationRecord.getApplyTimes()+1:1);
        authorizationRecord.increaseSeconds(authorizationRecord.getIncreaseSeconds()!=null?authorizationRecord.getIncreaseSeconds()+Constants.INCREASE_MINUTES:Constants.INCREASE_MINUTES);
        authorizationRecordRepository.save(authorizationRecord);
        //找到该授权的定时任务
        QuartzTask quartzTaskDelay = quartzTaskRepository.findByBusinessIdAndJobGroup(id,Constants.QUARTZ_GROUP_DELAY);
        if(quartzTaskDelay ==null){
            throw new BankServiceException("查询定时任务失败!");
        }
        Date date  = Date.from(expriationTime.toInstant());
        String triggerName = BankUtil.getCron(date);
        quartzTaskDelay.triggerTime(triggerName);
        quartzTaskRepository.save(quartzTaskDelay);
        QuartzTaskDTO quartzTaskDTOForDelayCheck = quartzTaskMapper.toDto(quartzTaskDelay);
        quartzTaskService.modifyTriggerTime(quartzTaskDTOForDelayCheck);
        //找到该发送记录的消息提醒的定时任务
        QuartzTask quartzTaskNotice = quartzTaskRepository.findByBusinessIdAndJobGroup(authId,Constants.QUARTZ_GROUP_MESSAGE);
        if(quartzTaskNotice ==null){
            throw new BankServiceException("查询定时任务失败!");
        }
        ZonedDateTime noticeTime = expriationTime.minusHours(1);
        Date dateNotice  = Date.from(noticeTime.toInstant());
        String triggerNameForNotice = BankUtil.getCron(dateNotice);
        quartzTaskNotice.triggerTime(triggerNameForNotice);
        quartzTaskRepository.save(quartzTaskNotice);
        QuartzTaskDTO quartzTaskDTOForNotice= quartzTaskMapper.toDto(quartzTaskNotice);
        quartzTaskService.modifyTriggerTime(quartzTaskDTOForNotice);
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);
        sendRecordDTO.setExpirationTime(expriationTime);
        return sendRecordDTO;
    }
}
