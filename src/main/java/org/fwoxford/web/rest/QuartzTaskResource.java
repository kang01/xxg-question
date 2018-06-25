package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.QuartzTaskService;
import org.fwoxford.service.dto.QuartzTaskDTO;
import org.fwoxford.web.rest.errors.BadRequestAlertException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.QuartzTaskCriteria;
import org.fwoxford.service.QuartzTaskQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QuartzTask.
 */
@RestController
@RequestMapping("/api")
public class QuartzTaskResource {

    private final Logger log = LoggerFactory.getLogger(QuartzTaskResource.class);

    private static final String ENTITY_NAME = "quartsTask";

    private final QuartzTaskService quartzTaskService;

    private final QuartzTaskQueryService quartzTaskQueryService;

    public QuartzTaskResource(QuartzTaskService quartzTaskService, QuartzTaskQueryService quartzTaskQueryService) {
        this.quartzTaskService = quartzTaskService;
        this.quartzTaskQueryService = quartzTaskQueryService;
    }

    /**
     * POST  /quarts-tasks : Create a new quartsTask.
     *
     * @param quartzTaskDTO the quartzTaskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quartzTaskDTO, or with status 400 (Bad Request) if the quartsTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/quarts-tasks")
    @Timed
    public ResponseEntity<QuartzTaskDTO> createQuartsTask(@Valid @RequestBody QuartzTaskDTO quartzTaskDTO) throws URISyntaxException {
        log.debug("REST request to save QuartzTask : {}", quartzTaskDTO);
        if (quartzTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new quartsTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuartzTaskDTO result = quartzTaskService.save(quartzTaskDTO);
        return ResponseEntity.created(new URI("/api/quarts-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /quarts-tasks : Updates an existing quartsTask.
     *
     * @param quartzTaskDTO the quartzTaskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated quartzTaskDTO,
     * or with status 400 (Bad Request) if the quartzTaskDTO is not valid,
     * or with status 500 (Internal Server Error) if the quartzTaskDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/quarts-tasks")
    @Timed
    public ResponseEntity<QuartzTaskDTO> updateQuartsTask(@Valid @RequestBody QuartzTaskDTO quartzTaskDTO) throws URISyntaxException {
        log.debug("REST request to update QuartzTask : {}", quartzTaskDTO);
        if (quartzTaskDTO.getId() == null) {
            return createQuartsTask(quartzTaskDTO);
        }
        QuartzTaskDTO result = quartzTaskService.save(quartzTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, quartzTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /quarts-tasks : get all the quartsTasks.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of quartsTasks in body
     */
    @GetMapping("/quarts-tasks")
    @Timed
    public ResponseEntity<List<QuartzTaskDTO>> getAllQuartsTasks(QuartzTaskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QuartsTasks by criteria: {}", criteria);
        Page<QuartzTaskDTO> page = quartzTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/quarts-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /quarts-tasks/:id : get the "id" quartsTask.
     *
     * @param id the id of the quartsTaskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the quartsTaskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/quarts-tasks/{id}")
    @Timed
    public ResponseEntity<QuartzTaskDTO> getQuartsTask(@PathVariable Long id) {
        log.debug("REST request to get QuartzTask : {}", id);
        QuartzTaskDTO quartzTaskDTO = quartzTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(quartzTaskDTO));
    }

    /**
     * DELETE  /quarts-tasks/:id : delete the "id" quartsTask.
     *
     * @param id the id of the quartsTaskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quarts-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuartsTask(@PathVariable Long id) {
        log.debug("REST request to delete QuartzTask : {}", id);
        quartzTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
