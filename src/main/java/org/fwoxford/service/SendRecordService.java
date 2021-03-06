package org.fwoxford.service;

import org.fwoxford.domain.AuthorizationRecord;
import org.fwoxford.domain.Question;
import org.fwoxford.domain.SendRecord;
import org.fwoxford.service.dto.SendRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing SendRecord.
 */
public interface SendRecordService {

    /**
     * Save a sendRecord.
     *
     * @param sendRecordDTO the entity to save
     * @return the persisted entity
     */
    SendRecordDTO save(SendRecordDTO sendRecordDTO);

    /**
     * Get all the sendRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SendRecordDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sendRecord.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SendRecordDTO findOne(Long id);

    /**
     * Delete the "id" sendRecord.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 发送邮件
     * @param question
     * @param authorizationRecordsForSave
     */
    List<SendRecordDTO>  sendEmailRecordToStranger(Question question, List<AuthorizationRecord> authorizationRecordsForSave);

    /**
     * 查询某一个问题的发送记录
     * @param id
     * @return
     */
    List<SendRecordDTO> findSendRecordByQuestionId(Long id);

    /**
     * 重发
     * @param id
     * @return
     */
    SendRecordDTO saveSendRecordForReSend(Long id);

    /**
     * 申请加时
     * @param id
     * @return
     */
    SendRecordDTO increaseTimeSendRecord(Long id);
}
