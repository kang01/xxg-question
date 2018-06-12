package org.fwoxford.service;

import org.fwoxford.service.dto.QuestionItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing QuestionItem.
 */
public interface QuestionItemService {

    /**
     * Save a questionItem.
     *
     * @param questionItemDTO the entity to save
     * @return the persisted entity
     */
    QuestionItemDTO save(QuestionItemDTO questionItemDTO);

    /**
     *  Get all the questionItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QuestionItemDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" questionItem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QuestionItemDTO findOne(Long id);

    /**
     *  Delete the "id" questionItem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 根据问题ID查询问题ITEM列表
     * @param id
     * @return
     */
    List<QuestionItemDTO> findQuestionItemByQuestionId(Long id);

    /**
     * 根据问题ID查询问题ITEM列表以及ITEM详情
     * @param id
     * @return
     */
    List<QuestionItemDTO> findQuestionItemAndDetailsByQuestionId(Long id);
}
