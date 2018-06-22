package org.fwoxford.service;


import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.fwoxford.domain.QuartsTask;
import org.fwoxford.domain.*; // for static metamodels
import org.fwoxford.repository.QuartsTaskRepository;
import org.fwoxford.service.dto.QuartsTaskCriteria;

import org.fwoxford.service.dto.QuartsTaskDTO;
import org.fwoxford.service.mapper.QuartsTaskMapper;

/**
 * Service for executing complex queries for QuartsTask entities in the database.
 * The main input is a {@link QuartsTaskCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuartsTaskDTO} or a {@link Page} of {@link QuartsTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuartsTaskQueryService extends QueryService<QuartsTask> {

    private final Logger log = LoggerFactory.getLogger(QuartsTaskQueryService.class);


    private final QuartsTaskRepository quartsTaskRepository;

    private final QuartsTaskMapper quartsTaskMapper;

    public QuartsTaskQueryService(QuartsTaskRepository quartsTaskRepository, QuartsTaskMapper quartsTaskMapper) {
        this.quartsTaskRepository = quartsTaskRepository;
        this.quartsTaskMapper = quartsTaskMapper;
    }

    /**
     * Return a {@link List} of {@link QuartsTaskDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuartsTaskDTO> findByCriteria(QuartsTaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<QuartsTask> specification = createSpecification(criteria);
        return quartsTaskMapper.toDto(quartsTaskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuartsTaskDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuartsTaskDTO> findByCriteria(QuartsTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<QuartsTask> specification = createSpecification(criteria);
        final Page<QuartsTask> result = quartsTaskRepository.findAll(specification, page);
        return result.map(quartsTaskMapper::toDto);
    }

    /**
     * Function to convert QuartsTaskCriteria to a {@link Specifications}
     */
    private Specifications<QuartsTask> createSpecification(QuartsTaskCriteria criteria) {
        Specifications<QuartsTask> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), QuartsTask_.id));
            }
            if (criteria.getJobName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobName(), QuartsTask_.jobName));
            }
            if (criteria.getTriggerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTriggerName(), QuartsTask_.triggerName));
            }
            if (criteria.getClassName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClassName(), QuartsTask_.className));
            }
            if (criteria.getEnableStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnableStatus(), QuartsTask_.enableStatus));
            }
            if (criteria.getTriggerTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTriggerTime(), QuartsTask_.triggerTime));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), QuartsTask_.status));
            }
            if (criteria.getMemo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMemo(), QuartsTask_.memo));
            }
        }
        return specification;
    }

}
