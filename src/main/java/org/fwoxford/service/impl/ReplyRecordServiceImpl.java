package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.Question;
import org.fwoxford.domain.ReplyDetails;
import org.fwoxford.domain.SendRecord;
import org.fwoxford.repository.*;
import org.fwoxford.service.ReplyRecordService;
import org.fwoxford.domain.ReplyRecord;
import org.fwoxford.service.dto.ReplyDetailsDTO;
import org.fwoxford.service.dto.ReplyRecordDTO;
import org.fwoxford.service.mapper.ReplyDetailsMapper;
import org.fwoxford.service.mapper.ReplyRecordMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing ReplyRecord.
 */
@Service
@Transactional
public class ReplyRecordServiceImpl implements ReplyRecordService {

    private final Logger log = LoggerFactory.getLogger(ReplyRecordServiceImpl.class);

    private final ReplyRecordRepository replyRecordRepository;

    private final ReplyRecordMapper replyRecordMapper;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionItemDetailsRepository questionItemDetailsRepository;
    @Autowired
    SendRecordRepository  sendRecordRepository;
    @Autowired
    ReplyDetailsMapper replyDetailsMapper;
    @Autowired
    ReplyDetailsRepository replyDetailsRepository;

    public ReplyRecordServiceImpl(ReplyRecordRepository replyRecordRepository, ReplyRecordMapper replyRecordMapper) {
        this.replyRecordRepository = replyRecordRepository;
        this.replyRecordMapper = replyRecordMapper;
    }

    /**
     * Save a replyRecord.
     *
     * @param replyRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReplyRecordDTO save(ReplyRecordDTO replyRecordDTO) {
        log.debug("Request to save ReplyRecord : {}", replyRecordDTO);
        ReplyRecord replyRecord = replyRecordMapper.replyRecordDTOToReplyRecord(replyRecordDTO);
        replyRecord = replyRecordRepository.save(replyRecord);
        return replyRecordMapper.replyRecordToReplyRecordDTO(replyRecord);
    }

    /**
     * Get all the replyRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReplyRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReplyRecords");
        return replyRecordRepository.findAll(pageable)
            .map(replyRecordMapper::replyRecordToReplyRecordDTO);
    }

    /**
     * Get one replyRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ReplyRecordDTO findOne(Long id) {
        log.debug("Request to get ReplyRecord : {}", id);
        ReplyRecord replyRecord = replyRecordRepository.findOne(id);
        return replyRecordMapper.replyRecordToReplyRecordDTO(replyRecord);
    }

    /**
     * Delete the replyRecord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReplyRecord : {}", id);
        replyRecordRepository.delete(id);
    }

    /**
     * 回复问题
     * @param sendRecordId
     * @param replyDetailsDTOS
     * @return
     */
    @Override
    public List<ReplyDetailsDTO> saveReplyQuestionList(Long sendRecordId, List<ReplyDetailsDTO> replyDetailsDTOS) {
        //验证传递参数中是否重复的问题样本ID
        Map<Long,List<ReplyDetailsDTO>> mapGroupByQuestionItemDetailsId = replyDetailsDTOS.stream().collect(Collectors.groupingBy(s->s.getQuestionItemDetailsId()));
        for(List<ReplyDetailsDTO> s :mapGroupByQuestionItemDetailsId.values()){
            if(s.size()>1){
                throw new BankServiceException("请勿提交重复的问题样本ID！");
            }
        }
        //获取发送记录
        SendRecord sendRecord = sendRecordRepository.findOne(sendRecordId);
        if(sendRecord == null){
            throw new BankServiceException("未找到发送记录！",sendRecordId.toString());
        }
        //发送记录，回复中
        sendRecord.status(Constants.QUESTION_SEND_REPLY_PENDING);
        sendRecordRepository.save(sendRecord);
        //根据发送记录找到问题
        Long questionId = sendRecord.getQuestionId();
        if(questionId == null){
            throw new BankServiceException("未找到对应的问题记录！",sendRecordId.toString());
        }
        Question question = questionRepository.findOne(questionId);
        if(question == null){
            throw new BankServiceException("问题不存在！",questionId.toString());
        }
        //当问题为已结束（已关闭）状态，不能在回复
        if(question.getStatus().equals(Constants.QUESTION_FINISHED)){
            throw new BankServiceException("问题已结束，不能再回复！");
        }
        //先找到回复记录，没有则创建，状态回复中
        ReplyRecord replyRecord = replyRecordRepository.findBySendRecordId(sendRecordId);
        if(replyRecord == null){
            replyRecord = new ReplyRecord();
        }
        replyRecord.status(Constants.QUESTION_REPLY_PENDING).strangerEmail(sendRecord.getStrangerEmail()).sendRecord(sendRecord)
            .strangerName(sendRecord.getStrangerName()).questionId(questionId).questionCode(question.getQuestionCode());
        replyRecordRepository.save(replyRecord);
        //每个问题样本在一次回复中只能有一个回复记录
        List<Long> questionItemDetailsIds = replyDetailsDTOS.stream().map(s->s.getQuestionItemDetailsId()).collect(Collectors.toList());
        //历史回复消息
        List<ReplyDetails> replyDetailsOldList = new ArrayList<>();
        List<List<Long>> questionItemDetailsIdsEach1000 = Lists.partition(questionItemDetailsIds,1000);
        for(List<Long> qids :questionItemDetailsIdsEach1000){
            List<ReplyDetails> replyDetailsOldListEach1000 = replyDetailsRepository.findByReplyRecordIdAndQuestionItemDetailsIdIn(replyRecord.getId(),qids);
            replyDetailsOldList.addAll(replyDetailsOldListEach1000);
        }
        //保存回复的详情，其中，回复类型编码不能为空，问题ITEM ID不能为空
        List<ReplyDetails> replyDetailss = new ArrayList<>();
        for(ReplyDetailsDTO replyDetailsDTO : replyDetailsDTOS){
            ReplyDetails replyDetailsOld = replyDetailsOldList.stream().filter(s->s.getQuestionItemDetails().getId().equals(replyDetailsDTO.getId())).findFirst().orElse(null);
            if(replyDetailsOld != null && replyDetailsDTO.getId() == null){
                throw new BankServiceException(replyDetailsOld.getQuestionItemDetails().getFrozenTube().getSampleCode()+"已经回复过，不能再回复！");
            }
            replyDetailsDTO.setReplyRecordId(replyRecord.getId());
            ReplyDetails replyDetails = replyDetailsMapper.toEntity(replyDetailsDTO);
            replyDetailss.add(replyDetails);
        }
        replyDetailsRepository.save(replyDetailss);

        return replyDetailsMapper.toDto(replyDetailss);
    }

    /**
     * 回复完成
     * @param id
     * @return
     */
    @Override
    public ReplyRecordDTO completedReplyRecord(Long id) {
        ReplyRecord replyRecord = replyRecordRepository.findOne(id);
        if(replyRecord == null){
            throw new BankServiceException("未查询到回复内容！");
        }
        if(!replyRecord.getStatus().equals(Constants.QUESTION_REPLY_PENDING)){
            throw new BankServiceException("该回复不在进行中，不能完成！");
        }
        //回复完成
        replyRecord.status(Constants.QUESTION_REPLY_FINISHED);
        replyRecordRepository.save(replyRecord);
        //发送表的状态更改为已回复
        SendRecord sendRecord = replyRecord.getSendRecord();
        sendRecord.status(Constants.QUESTION_SEND_REPLIED);
        sendRecord.replyDate(ZonedDateTime.now());
        sendRecordRepository.save(sendRecord);
        //判断问题是否都已经回复完成
        //如果都完成了,问题的状态为已回复，否则状态为有回复
        Long countOfUnReplyRecord = sendRecordRepository.countUnReplyRecordByQuestionId(sendRecord.getQuestionId());
        Question question = questionRepository.findOne(sendRecord.getQuestionId());
        if(question == null){
            throw new BankServiceException("未查询到问题记录！");
        }
        if(countOfUnReplyRecord == null || (countOfUnReplyRecord!=null &&countOfUnReplyRecord==0)){
            question = question.status(Constants.QUESTION_REPLY);
        }else{
            question = question.status(Constants.QUESTION_REPLY_PART);
        }
        questionRepository.save(question);

        return replyRecordMapper.replyRecordToReplyRecordDTO(replyRecord);
    }
}
