package org.fwoxford.service;

import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing QuestionItemDetails.
 */
public interface QuestionItemDetailsService {

    /**
     * Save a questionItemDetails.
     *
     * @param questionItemDetailsDTO the entity to save
     * @return the persisted entity
     */
    QuestionItemDetailsDTO save(QuestionItemDetailsDTO questionItemDetailsDTO);

    /**
     *  Get all the questionItemDetails.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QuestionItemDetailsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" questionItemDetails.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QuestionItemDetailsDTO findOne(Long id);

    /**
     *  Delete the "id" questionItemDetails.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
