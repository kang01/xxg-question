package org.fwoxford.service.impl;

import org.fwoxford.domain.QuartzTask;
import org.fwoxford.service.QuartzTaskService;
import org.fwoxford.repository.QuartzTaskRepository;
import org.fwoxford.service.dto.QuartzTaskDTO;
import org.fwoxford.service.mapper.QuartzTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing QuartzTask.
 */
@Service
@Transactional
public class QuartzTaskServiceImpl implements QuartzTaskService {

    private final Logger log = LoggerFactory.getLogger(QuartzTaskServiceImpl.class);

    private final QuartzTaskRepository quartzTaskRepository;

    private final QuartzTaskMapper quartzTaskMapper;

    public QuartzTaskServiceImpl(QuartzTaskRepository quartzTaskRepository, QuartzTaskMapper quartzTaskMapper) {
        this.quartzTaskRepository = quartzTaskRepository;
        this.quartzTaskMapper = quartzTaskMapper;
    }

    /**
     * Save a quartsTask.
     *
     * @param quartzTaskDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuartzTaskDTO save(QuartzTaskDTO quartzTaskDTO) {
        log.debug("Request to save QuartzTask : {}", quartzTaskDTO);
        QuartzTask quartzTask = quartzTaskMapper.toEntity(quartzTaskDTO);
        quartzTask = quartzTaskRepository.save(quartzTask);
        return quartzTaskMapper.toDto(quartzTask);
    }

    /**
     * Get all the quartsTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuartzTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuartsTasks");
        return quartzTaskRepository.findAll(pageable)
            .map(quartzTaskMapper::toDto);
    }

    /**
     * Get one quartsTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QuartzTaskDTO findOne(Long id) {
        log.debug("Request to get QuartzTask : {}", id);
        QuartzTask quartzTask = quartzTaskRepository.findOne(id);
        return quartzTaskMapper.toDto(quartzTask);
    }

    /**
     * Delete the quartsTask by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuartzTask : {}", id);
        quartzTaskRepository.delete(id);
    }
}
