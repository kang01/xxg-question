package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.service.QuestionItemService;
import org.fwoxford.service.dto.QuestionItemDTO;
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
 * REST controller for managing QuestionItem.
 */
@RestController
@RequestMapping("/api")
public class QuestionItemResource {

    private final Logger log = LoggerFactory.getLogger(QuestionItemResource.class);

    private static final String ENTITY_NAME = "questionItem";

    private final QuestionItemService questionItemService;

    public QuestionItemResource(QuestionItemService questionItemService) {
        this.questionItemService = questionItemService;
    }

    /**
     * POST  /question-items : Create a new questionItem.
     *
     * @param questionItemDTO the questionItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questionItemDTO, or with status 400 (Bad Request) if the questionItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/question-items")
    @Timed
    public ResponseEntity<QuestionItemDTO> createQuestionItem(@Valid @RequestBody QuestionItemDTO questionItemDTO) throws URISyntaxException {
        log.debug("REST request to save QuestionItem : {}", questionItemDTO);
        if (questionItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new questionItem cannot already have an ID")).body(null);
        }
        QuestionItemDTO result = questionItemService.save(questionItemDTO);
        return ResponseEntity.created(new URI("/api/question-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /question-items : Updates an existing questionItem.
     *
     * @param questionItemDTO the questionItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questionItemDTO,
     * or with status 400 (Bad Request) if the questionItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the questionItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/question-items")
    @Timed
    public ResponseEntity<QuestionItemDTO> updateQuestionItem(@Valid @RequestBody QuestionItemDTO questionItemDTO) throws URISyntaxException {
        log.debug("REST request to update QuestionItem : {}", questionItemDTO);
        if (questionItemDTO.getId() == null) {
          //  throw new BankServiceException("问题ITEM的ID不能为空！");
        }
        QuestionItemDTO result = questionItemService.save(questionItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /question-items : get all the questionItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of questionItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/question-items")
    @Timed
    public ResponseEntity<List<QuestionItemDTO>> getAllQuestionItems(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of QuestionItems");
        Page<QuestionItemDTO> page = questionItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/question-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /question-items/:id : get the "id" questionItem.
     *
     * @param id the id of the questionItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questionItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/question-items/{id}")
    @Timed
    public ResponseEntity<QuestionItemDTO> getQuestionItem(@PathVariable Long id) {
        log.debug("REST request to get QuestionItem : {}", id);
        QuestionItemDTO questionItemDTO = questionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionItemDTO));
    }

    /**
     * DELETE  /question-items/:id : delete the "id" questionItem.
     *
     * @param id the id of the questionItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/question-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuestionItem(@PathVariable Long id) {
        log.debug("REST request to delete QuestionItem : {}", id);
        questionItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据问题ID查询问题ITEM列表
     * @param id
     * @return
     */
    @GetMapping("/question-items/question/{id}")
    @Timed
    public ResponseEntity<List<QuestionItemDTO>> getQuestionItemByQuestionId(@PathVariable Long id) {
        log.debug("REST request to get QuestionItem : {}", id);
        List<QuestionItemDTO> questionItemDTOs = questionItemService.findQuestionItemByQuestionId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionItemDTOs));
    }

    /**
     * 根据问题ID查询问题ITEM列表以及ITEM详情
     * @param id
     * @return
     */
    @GetMapping("/question-items/items-details/question/{id}")
    @Timed
    public ResponseEntity<List<QuestionItemDTO>> getQuestionItemAndDetailsByQuestionId(@PathVariable Long id) {
        log.debug("REST request to get QuestionItem : {}", id);
        List<QuestionItemDTO> questionItemDTOs = questionItemService.findQuestionItemAndDetailsByQuestionId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionItemDTOs));
    }
}
