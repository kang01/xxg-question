package org.fwoxford.service;

import org.fwoxford.service.dto.ReplyDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ReplyDetails.
 */
public interface ReplyDetailsService {

    /**
     * Save a replyDetails.
     *
     * @param replyDetailsDTO the entity to save
     * @return the persisted entity
     */
    ReplyDetailsDTO save(ReplyDetailsDTO replyDetailsDTO);

    /**
     * Get all the replyDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReplyDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" replyDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ReplyDetailsDTO findOne(Long id);

    /**
     * Delete the "id" replyDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
