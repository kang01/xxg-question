package org.fwoxford.service;


import java.util.List;

import org.fwoxford.domain.QuartzTask_;
import org.fwoxford.service.dto.QuartzTaskCriteria;
import org.fwoxford.service.dto.QuartzTaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.fwoxford.domain.QuartzTask;
import org.fwoxford.repository.QuartzTaskRepository;

import org.fwoxford.service.mapper.QuartzTaskMapper;

/**
 * Service for executing complex queries for QuartzTask entities in the database.
 * The main input is a {@link QuartzTaskCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuartzTaskDTO} or a {@link Page} of {@link QuartzTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuartzTaskQueryService extends QueryService<QuartzTask> {

    private final Logger log = LoggerFactory.getLogger(QuartzTaskQueryService.class);


    private final QuartzTaskRepository quartzTaskRepository;

    private final QuartzTaskMapper quartzTaskMapper;

    public QuartzTaskQueryService(QuartzTaskRepository quartzTaskRepository, QuartzTaskMapper quartzTaskMapper) {
        this.quartzTaskRepository = quartzTaskRepository;
        this.quartzTaskMapper = quartzTaskMapper;
    }

    /**
     * Return a {@link List} of {@link QuartzTaskDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuartzTaskDTO> findByCriteria(QuartzTaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<QuartzTask> specification = createSpecification(criteria);
        return quartzTaskMapper.toDto(quartzTaskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuartzTaskDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuartzTaskDTO> findByCriteria(QuartzTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<QuartzTask> specification = createSpecification(criteria);
        final Page<QuartzTask> result = quartzTaskRepository.findAll(specification, page);
        return result.map(quartzTaskMapper::toDto);
    }

    /**
     * Function to convert QuartzTaskCriteria to a {@link Specifications}
     */
    private Specifications<QuartzTask> createSpecification(QuartzTaskCriteria criteria) {
        Specifications<QuartzTask> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), QuartzTask_.id));
            }
            if (criteria.getJobName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobName(), QuartzTask_.jobName));
            }
            if (criteria.getTriggerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTriggerName(), QuartzTask_.triggerName));
            }
            if (criteria.getClassName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClassName(), QuartzTask_.className));
            }
            if (criteria.getEnableStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnableStatus(), QuartzTask_.enableStatus));
            }
            if (criteria.getTriggerTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTriggerTime(), QuartzTask_.triggerTime));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), QuartzTask_.status));
            }
            if (criteria.getMemo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMemo(), QuartzTask_.memo));
            }
        }
        return specification;
    }

}
