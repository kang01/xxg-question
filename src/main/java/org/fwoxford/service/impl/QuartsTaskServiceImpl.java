package org.fwoxford.service.impl;

import org.fwoxford.service.QuartsTaskService;
import org.fwoxford.domain.QuartsTask;
import org.fwoxford.repository.QuartsTaskRepository;
import org.fwoxford.service.dto.QuartsTaskDTO;
import org.fwoxford.service.mapper.QuartsTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing QuartsTask.
 */
@Service
@Transactional
public class QuartsTaskServiceImpl implements QuartsTaskService {

    private final Logger log = LoggerFactory.getLogger(QuartsTaskServiceImpl.class);

    private final QuartsTaskRepository quartsTaskRepository;

    private final QuartsTaskMapper quartsTaskMapper;

    public QuartsTaskServiceImpl(QuartsTaskRepository quartsTaskRepository, QuartsTaskMapper quartsTaskMapper) {
        this.quartsTaskRepository = quartsTaskRepository;
        this.quartsTaskMapper = quartsTaskMapper;
    }

    /**
     * Save a quartsTask.
     *
     * @param quartsTaskDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuartsTaskDTO save(QuartsTaskDTO quartsTaskDTO) {
        log.debug("Request to save QuartsTask : {}", quartsTaskDTO);
        QuartsTask quartsTask = quartsTaskMapper.toEntity(quartsTaskDTO);
        quartsTask = quartsTaskRepository.save(quartsTask);
        return quartsTaskMapper.toDto(quartsTask);
    }

    /**
     * Get all the quartsTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuartsTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuartsTasks");
        return quartsTaskRepository.findAll(pageable)
            .map(quartsTaskMapper::toDto);
    }

    /**
     * Get one quartsTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QuartsTaskDTO findOne(Long id) {
        log.debug("Request to get QuartsTask : {}", id);
        QuartsTask quartsTask = quartsTaskRepository.findOne(id);
        return quartsTaskMapper.toDto(quartsTask);
    }

    /**
     * Delete the quartsTask by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuartsTask : {}", id);
        quartsTaskRepository.delete(id);
    }
}
