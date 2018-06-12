package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.service.QuestionItemDetailsService;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
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
 * REST controller for managing QuestionItemDetails.
 */
@RestController
@RequestMapping("/api")
public class QuestionItemDetailsResource {

    private final Logger log = LoggerFactory.getLogger(QuestionItemDetailsResource.class);

    private static final String ENTITY_NAME = "questionItemDetails";

    private final QuestionItemDetailsService questionItemDetailsService;

    public QuestionItemDetailsResource(QuestionItemDetailsService questionItemDetailsService) {
        this.questionItemDetailsService = questionItemDetailsService;
    }

    /**
     * POST  /question-item-details : Create a new questionItemDetails.
     *
     * @param questionItemDetailsDTO the questionItemDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questionItemDetailsDTO, or with status 400 (Bad Request) if the questionItemDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/question-item-details")
    @Timed
    public ResponseEntity<QuestionItemDetailsDTO> createQuestionItemDetails(@Valid @RequestBody QuestionItemDetailsDTO questionItemDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save QuestionItemDetails : {}", questionItemDetailsDTO);
        if (questionItemDetailsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new questionItemDetails cannot already have an ID")).body(null);
        }
        QuestionItemDetailsDTO result = questionItemDetailsService.save(questionItemDetailsDTO);
        return ResponseEntity.created(new URI("/api/question-item-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /question-item-details : Updates an existing questionItemDetails.
     *
     * @param questionItemDetailsDTO the questionItemDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questionItemDetailsDTO,
     * or with status 400 (Bad Request) if the questionItemDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the questionItemDetailsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/question-item-details")
    @Timed
    public ResponseEntity<QuestionItemDetailsDTO> updateQuestionItemDetails(@Valid @RequestBody QuestionItemDetailsDTO questionItemDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update QuestionItemDetails : {}", questionItemDetailsDTO);
        if (questionItemDetailsDTO.getId() == null) {
            return createQuestionItemDetails(questionItemDetailsDTO);
        }
        QuestionItemDetailsDTO result = questionItemDetailsService.save(questionItemDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionItemDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /question-item-details : get all the questionItemDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of questionItemDetails in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/question-item-details")
    @Timed
    public ResponseEntity<List<QuestionItemDetailsDTO>> getAllQuestionItemDetails(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of QuestionItemDetails");
        Page<QuestionItemDetailsDTO> page = questionItemDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/question-item-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /question-item-details/:id : get the "id" questionItemDetails.
     *
     * @param id the id of the questionItemDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questionItemDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/question-item-details/{id}")
    @Timed
    public ResponseEntity<QuestionItemDetailsDTO> getQuestionItemDetails(@PathVariable Long id) {
        log.debug("REST request to get QuestionItemDetails : {}", id);
        QuestionItemDetailsDTO questionItemDetailsDTO = questionItemDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionItemDetailsDTO));
    }

    /**
     * DELETE  /question-item-details/:id : delete the "id" questionItemDetails.
     *
     * @param id the id of the questionItemDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/question-item-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuestionItemDetails(@PathVariable Long id) {
        log.debug("REST request to delete QuestionItemDetails : {}", id);
        questionItemDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
