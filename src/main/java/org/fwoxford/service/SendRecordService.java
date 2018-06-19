package org.fwoxford.service;

import org.fwoxford.service.dto.SendRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
