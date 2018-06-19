package org.fwoxford.service;

import org.fwoxford.service.dto.ReplyRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
