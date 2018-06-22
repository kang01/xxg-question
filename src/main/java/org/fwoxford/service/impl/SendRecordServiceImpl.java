package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.AuthorizationRecord;
import org.fwoxford.domain.Question;
import org.fwoxford.repository.AuthorizationRecordRepository;
import org.fwoxford.repository.QuestionItemDetailsRepository;
import org.fwoxford.repository.QuestionRepository;
import org.fwoxford.service.MailService;
import org.fwoxford.service.SendRecordService;
import org.fwoxford.domain.SendRecord;
import org.fwoxford.repository.SendRecordRepository;
import org.fwoxford.service.dto.EmailMessage;
import org.fwoxford.service.dto.MessagerDTO;
import org.fwoxford.service.dto.SendRecordDTO;
import org.fwoxford.service.mapper.SendRecordMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    /**
     * 发送邮件
     * @param questionId
     * @return
     */
    @Override
    public SendRecordDTO sendRecord(Long questionId) {

        return null;
    }

    @Override
    public void sendEmailRecordToStranger(Question question, List<AuthorizationRecord> authorizationRecordsForSave) {
        //一个问题只能发送给一个人一次（不在过期状态）
        List<SendRecord> sendRecordsOldList = sendRecordRepository.findByQuestionIdAndStatusNot(question.getId(),Constants.QUESTION_SEND_OVERDUE);
        List<SendRecord> sendRecords = new ArrayList<>();
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
            MessagerDTO messagerDTO = new MessagerDTO();
            messagerDTO.setFromUser("gengluy@163.com");
            messagerDTO.setToUser(authorizationRecord.getStrangerEmail());
            mailService.sendMessageMail(emailMessage, question.getQuestionSummary(), "message.ftl",messagerDTO);
            SendRecord sendRecord = new SendRecord();
            sendRecord.questionCode(question.getQuestionCode()).questionId(question.getId())
                .authorizationRecordId(authorizationRecord.getId()).status(Constants.QUESTION_SENT)
                .senderEmail("gengluy@163.com").strangerName(authorizationRecord.getStrangerName()).strangerEmail(authorizationRecord.getStrangerEmail());
            sendRecords.add(sendRecord);
        }
        sendRecordRepository.save(sendRecords);
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
        if(sendRecord ==null || !sendRecord.equals(Constants.QUESTION_SEND_OVERDUE)){
            throw new BankServiceException("上一次发送未过期，无法再次发送！");
        }
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

        authorizationRecord.setExpirationTime(Constants.EXPRIATIONTIME);
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
        MessagerDTO messagerDTO = new MessagerDTO();
        messagerDTO.setFromUser("gengluy@163.com");
        messagerDTO.setToUser(authorizationRecord.getStrangerEmail());
        mailService.sendMessageMail(emailMessage, question.getQuestionSummary(), "message.ftl",messagerDTO);
        SendRecord sendRecordNew = new SendRecord();
        sendRecordNew.questionCode(question.getQuestionCode()).questionId(question.getId())
            .authorizationRecordId(authorizationRecord.getId()).status(Constants.VALID)
            .senderEmail("gengluy@163.com").strangerName(authorizationRecord.getStrangerName()).strangerEmail(authorizationRecord.getStrangerEmail());
        sendRecordRepository.save(sendRecordNew);
        return sendRecordMapper.sendRecordToSendRecordDTO(sendRecordNew);
    }
}
