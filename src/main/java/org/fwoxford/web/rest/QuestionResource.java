package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.service.MailService;
import org.fwoxford.service.QuestionService;
import org.fwoxford.service.dto.QuestionDTO;
import org.fwoxford.service.dto.ReplyRecordDTO;
import org.fwoxford.service.dto.response.QuestionForDataTableEntity;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Question.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    private final QuestionService questionService;

    public QuestionResource(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * POST  /questions : Create a new question.
     *
     * @param questionDTO the questionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questionDTO, or with status 400 (Bad Request) if the question has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/questions")
    @Timed
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new question cannot already have an ID")).body(null);
        }
        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 新增保存问题，问题ITEM，问题ITEM 详情
     * @param questionDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/questions/items/details")
    @Timed
    public ResponseEntity<QuestionDTO> createQuestionAndItemsAndDetails(@Valid @RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new question cannot already have an ID")).body(null);
        }
        QuestionDTO result = questionService.saveQuestionAndItemsAndDetails(questionDTO);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 修改保存问题，问题ITEM，问题ITEM 详情
     * @param questionDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/questions/items/details")
    @Timed
    public ResponseEntity<QuestionDTO> updateQuestionAndItemsAndDetails(@Valid @RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() == null) {
            return createQuestionAndItemsAndDetails(questionDTO);
        }
        QuestionDTO result = questionService.saveQuestionAndItemsAndDetails(questionDTO);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /questions : Updates an existing question.
     *
     * @param questionDTO the questionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questionDTO,
     * or with status 400 (Bad Request) if the questionDTO is not valid,
     * or with status 500 (Internal Server Error) if the questionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/questions")
    @Timed
    public ResponseEntity<QuestionDTO> updateQuestion(@Valid @RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to update Question : {}", questionDTO);
        if (questionDTO.getId() == null) {
            return createQuestion(questionDTO);
        }
        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questions : get all the questions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of questions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/questions")
    @Timed
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Questions");
        Page<QuestionDTO> page = questionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /questions/:id : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/questions/{id}")
    @Timed
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        QuestionDTO questionDTO = questionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionDTO));
    }

    /**
     * DELETE  /questions/:id : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/questions/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);
        questionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 分页查询问题清单
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/questions", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<QuestionForDataTableEntity> getPageQuestions(@RequestBody DataTablesInput input) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<QuestionForDataTableEntity> questionForDataTableEntityDataTablesOutput = questionService.findPageQuestions(input);
        return questionForDataTableEntityDataTablesOutput;
    }

    /**
     * 根据问题ID查询问题详情以及ITEM LIST , ITEM DETAILS
     * @param id
     * @return
     */
    @GetMapping("/questions/items/details/{id}")
    @Timed
    public ResponseEntity<QuestionDTO> getQuestionAndItemsAndDetails(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        QuestionDTO questionDTO = questionService.findQuestionAndItemsAndDetails(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionDTO));
    }


    /**
     * 陌生人查询问题详情以及回复内容列表
     * @param id
     * @return
     */
    @GetMapping("/question/reply-records/sendRecord/{id}")
    @Timed
    public ResponseEntity<QuestionDTO> getReplyRecordAndQuestionDetails(@PathVariable Long id) {
        log.debug("REST request to get ReplyRecord : {}", id);
        QuestionDTO questionDTO = questionService.findReplyRecordAndQuestionDetails(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionDTO));
    }
}
