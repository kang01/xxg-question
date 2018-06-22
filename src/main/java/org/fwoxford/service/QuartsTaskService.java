package org.fwoxford.service;

import org.fwoxford.service.dto.QuartsTaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing QuartsTask.
 */
public interface QuartsTaskService {

    /**
     * Save a quartsTask.
     *
     * @param quartsTaskDTO the entity to save
     * @return the persisted entity
     */
    QuartsTaskDTO save(QuartsTaskDTO quartsTaskDTO);

    /**
     * Get all the quartsTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuartsTaskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" quartsTask.
     *
     * @param id the id of the entity
     * @return the entity
     */
    QuartsTaskDTO findOne(Long id);

    /**
     * Delete the "id" quartsTask.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
