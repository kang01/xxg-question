package org.fwoxford.service;

import org.fwoxford.service.dto.ReplyDetailsDTO;
import org.fwoxford.service.dto.ReplyRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ReplyRecord.
 */
public interface ReplyRecordService {

    /**
     * Save a replyRecord.
     *
     * @param replyRecordDTO the entity to save
     * @return the persisted entity
     */
    ReplyRecordDTO save(ReplyRecordDTO replyRecordDTO);

    /**
     * Get all the replyRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReplyRecordDTO> findAll(Pageable pageable);

    /**
     * Get the "id" replyRecord.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ReplyRecordDTO findOne(Long id);

    /**
     * Delete the "id" replyRecord.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 回复问题
     * @param replyRecordDTO
     * @return
     */
    ReplyRecordDTO saveReplyQuestionList(ReplyRecordDTO replyRecordDTO);

    /**
     * 回复完成
     * @param sendRecordId
     * @return
     */
    ReplyRecordDTO completedReplyRecord(Long sendRecordId);
}
