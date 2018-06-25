package org.fwoxford.service;

import org.fwoxford.service.dto.QuartzTaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing QuartzTask.
 */
public interface QuartzTaskService {

    /**
     * Save a quartsTask.
     *
     * @param quartzTaskDTO the entity to save
     * @return the persisted entity
     */
    QuartzTaskDTO save(QuartzTaskDTO quartzTaskDTO);

    /**
     * Get all the quartsTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuartzTaskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" quartsTask.
     *
     * @param id the id of the entity
     * @return the entity
     */
    QuartzTaskDTO findOne(Long id);

    /**
     * Delete the "id" quartsTask.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
