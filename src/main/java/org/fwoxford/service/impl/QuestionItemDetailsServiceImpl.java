package org.fwoxford.service.impl;

import org.fwoxford.domain.QuestionItemDetails;
import org.fwoxford.repository.QuestionItemDetailsRepository;
import org.fwoxford.service.QuestionItemDetailsService;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.service.mapper.QuestionItemDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing QuestionItemDetails.
 */
@Service
@Transactional
public class QuestionItemDetailsServiceImpl implements QuestionItemDetailsService {

    private final Logger log = LoggerFactory.getLogger(QuestionItemDetailsServiceImpl.class);

    private final QuestionItemDetailsRepository questionItemDetailsRepository;

    private final QuestionItemDetailsMapper questionItemDetailsMapper;

    public QuestionItemDetailsServiceImpl(QuestionItemDetailsRepository questionItemDetailsRepository, QuestionItemDetailsMapper questionItemDetailsMapper) {
        this.questionItemDetailsRepository = questionItemDetailsRepository;
        this.questionItemDetailsMapper = questionItemDetailsMapper;
    }

    /**
     * Save a questionItemDetails.
     *
     * @param questionItemDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuestionItemDetailsDTO save(QuestionItemDetailsDTO questionItemDetailsDTO) {
        log.debug("Request to save QuestionItemDetails : {}", questionItemDetailsDTO);
        QuestionItemDetails questionItemDetails = questionItemDetailsMapper.questionItemDetailsDTOToQuestionItemDetails(questionItemDetailsDTO);
        questionItemDetails = questionItemDetailsRepository.save(questionItemDetails);
        QuestionItemDetailsDTO result = questionItemDetailsMapper.questionItemDetailsToQuestionItemDetailsDTO(questionItemDetails);
        return result;
    }

    /**
     *  Get all the questionItemDetails.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionItemDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionItemDetails");
        Page<QuestionItemDetails> result = questionItemDetailsRepository.findAll(pageable);
        return result.map(questionItemDetails -> questionItemDetailsMapper.questionItemDetailsToQuestionItemDetailsDTO(questionItemDetails));
    }

    /**
     *  Get one questionItemDetails by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QuestionItemDetailsDTO findOne(Long id) {
        log.debug("Request to get QuestionItemDetails : {}", id);
        QuestionItemDetails questionItemDetails = questionItemDetailsRepository.findOne(id);
        QuestionItemDetailsDTO questionItemDetailsDTO = questionItemDetailsMapper.questionItemDetailsToQuestionItemDetailsDTO(questionItemDetails);
        return questionItemDetailsDTO;
    }

    /**
     *  Delete the  questionItemDetails by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestionItemDetails : {}", id);
        questionItemDetailsRepository.delete(id);
    }
}
