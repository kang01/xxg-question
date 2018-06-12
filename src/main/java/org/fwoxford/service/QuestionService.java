package org.fwoxford.service;

import org.fwoxford.service.dto.QuestionDTO;
import org.fwoxford.service.dto.response.QuestionForDataTableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

/**
 * Service Interface for managing Question.
 */
public interface QuestionService {

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    QuestionDTO save(QuestionDTO questionDTO);

    /**
     *  Get all the questions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QuestionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" question.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QuestionDTO findOne(Long id);

    /**
     *  Delete the "id" question.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 分页查询问题清单
     * @param input
     * @return
     */
    DataTablesOutput<QuestionForDataTableEntity> findPageQuestions(DataTablesInput input);

    /**
     *  保存问题，问题ITEM，问题ITEM 详情
     * @param questionDTO
     * @return
     */
    QuestionDTO saveQuestionAndItemsAndDetails(QuestionDTO questionDTO);

    /**
     * 根据问题ID查询问题详情以及ITEM LIST , ITEM DETAILS
     * @param id
     * @return
     */
    QuestionDTO findQuestionAndItemsAndDetails(Long id);
}
