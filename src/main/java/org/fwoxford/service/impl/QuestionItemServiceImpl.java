package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.domain.Question;
import org.fwoxford.domain.QuestionItem;
import org.fwoxford.domain.QuestionItemDetails;
import org.fwoxford.repository.QuestionItemDetailsRepository;
import org.fwoxford.repository.QuestionItemRepository;
import org.fwoxford.repository.QuestionRepository;
import org.fwoxford.service.QuestionItemService;
import org.fwoxford.service.dto.QuestionItemDTO;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.service.mapper.QuestionItemDetailsMapper;
import org.fwoxford.service.mapper.QuestionItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing QuestionItem.
 */
@Service
@Transactional
public class QuestionItemServiceImpl implements QuestionItemService {

    private final Logger log = LoggerFactory.getLogger(QuestionItemServiceImpl.class);

    private final QuestionItemRepository questionItemRepository;

    private final QuestionItemMapper questionItemMapper;

    @Autowired
    QuestionItemDetailsRepository questionItemDetailsRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionItemDetailsMapper questionItemDetailsMapper;

    public QuestionItemServiceImpl(QuestionItemRepository questionItemRepository, QuestionItemMapper questionItemMapper) {
        this.questionItemRepository = questionItemRepository;
        this.questionItemMapper = questionItemMapper;
    }

    /**
     * Save a questionItem.
     *
     * @param questionItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuestionItemDTO save(QuestionItemDTO questionItemDTO) {
        log.debug("Request to save QuestionItem : {}", questionItemDTO);
        QuestionItem questionItem = questionItemMapper.questionItemDTOToQuestionItem(questionItemDTO);
        Long questionId = questionItemDTO.getQuestionId();
        if(questionId == null){
            throw new BankServiceException("问题ID不能为空！");
        }
        if(questionItemDTO.getFrozenTubeIds()==null || questionItemDTO.getFrozenTubeIds().size()==0){
            throw new BankServiceException("未传入问题样本ID！");
        }
        Question question = questionRepository.findOne(questionId);
        if(question == null || (question!=null &&!question.getStatus().equals(Constants.QUESTION_IN_DRAFT))){
            throw new BankServiceException("问题"+question.getQuestionCode()+"已不再草拟中，不能编辑ITEM！");
        }
        questionItem.setStatus(Constants.VALID);
        questionItem = questionItemRepository.save(questionItem);
        questionItemDTO.setId(questionItem.getId());
        questionItemDTO.setStatus(questionItem.getStatus());
        List<QuestionItemDetails> questionItemDetailssForDel = new ArrayList<>();
        List<QuestionItemDetails> questionItemDetailssForSave = new ArrayList<>();
        if(questionItemDTO.getId()!=null){
            List<QuestionItemDetails> questionItemDetailssOld = questionItemDetailsRepository.findByQuestionItemIdAndStatusNot(
                questionItemDTO.getId(), Constants.INVALID);
            questionItemDetailssOld.forEach(s->{
                if(!questionItemDTO.getFrozenTubeIds().contains(s.getFrozenTube().getId())){
                    s.setStatus(Constants.INVALID);
                    questionItemDetailssForDel.add(s);
                }else{
                    questionItemDetailssForSave.add(s);
                }
            });
        }


        for(Long frozenTubeId :questionItemDTO.getFrozenTubeIds()){
            FrozenTube frozenTube = new FrozenTube();
            frozenTube.setId(frozenTubeId);
            QuestionItemDetails questionItemDetails = questionItemDetailssForSave.stream().filter(s->s.getFrozenTube().getId().equals(frozenTubeId)).findFirst().orElse(null);
            if(questionItemDetails == null){
                questionItemDetails = new QuestionItemDetails().questionItem(questionItem).frozenTube(frozenTube).status(Constants.VALID);
            }
            questionItemDetailssForSave.add(questionItemDetails);
        }
        questionItemDetailsRepository.save(questionItemDetailssForSave);
        questionItemDetailsRepository.save(questionItemDetailssForDel);
        return questionItemDTO;
    }

    /**
     *  Get all the questionItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionItems");
        Page<QuestionItem> result = questionItemRepository.findAll(pageable);
        return result.map(questionItem -> questionItemMapper.questionItemToQuestionItemDTO(questionItem));
    }

    /**
     *  Get one questionItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QuestionItemDTO findOne(Long id) {
        log.debug("Request to get QuestionItem : {}", id);
        QuestionItem questionItem = questionItemRepository.findByIdAndStatusNot(id, Constants.INVALID);
        if(questionItem == null){
            throw new BankServiceException("问题ITEM不存在！");
        }
        QuestionItemDTO questionItemDTO = questionItemMapper.questionItemToQuestionItemDTO(questionItem);
        List<QuestionItemDetails> questionItemDetailss = questionItemDetailsRepository.findByQuestionItemIdAndStatusNot(id, Constants.INVALID);
        List<QuestionItemDetailsDTO> questionItemDetailsDTOS = questionItemDetailsMapper.questionItemDetailsToQuestionItemDetailsDTOs(questionItemDetailss);
        questionItemDTO.setQuestionItemDetailsDTOS(questionItemDetailsDTOS);
        return questionItemDTO;
    }

    /**
     *  Delete the  questionItem by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestionItem : {}", id);
        QuestionItem questionItem = questionItemRepository.findOne(id);
        if(questionItem==null ){
            throw new BankServiceException("问题ITEM不存在!");
        }
        Question question = questionItem.getQuestion();
        if(!question.getStatus().equals(Constants.QUESTION_IN_DRAFT)){
            throw new BankServiceException("问题"+question.getQuestionCode()+"已不再草拟中，不能删除ITEM！");
        }
        questionItem.setStatus(Constants.INVALID);
        questionItemRepository.save(questionItem);
        questionItemDetailsRepository.updateStatusByQuestionItemquestionItemId(id);
    }

    /**
     * 根据问题ID查询问题ITEM列表
     * @param id
     * @return
     */
    @Override
    public List<QuestionItemDTO> findQuestionItemByQuestionId(Long id) {
        List<QuestionItem> questionItems = questionItemRepository.findByQuestionIdAndStatusNot(id, Constants.INVALID);
        return questionItemMapper.questionItemsToQuestionItemDTOs(questionItems);
    }

    /**
     * 根据问题ID查询问题ITEM列表
     * @param id
     * @return
     */
    @Override
    public List<QuestionItemDTO> findQuestionItemAndDetailsByQuestionId(Long id) {

        List<QuestionItem> questionItems = questionItemRepository.findByQuestionIdAndStatusNot(id, Constants.INVALID);
        List<QuestionItemDTO> questionItemDTOS = questionItemMapper.questionItemsToQuestionItemDTOs(questionItems);
        for(QuestionItemDTO questionItemDTO :questionItemDTOS){
            List<QuestionItemDetails> questionItemDetailss = questionItemDetailsRepository.findByQuestionItemIdAndStatusNot(questionItemDTO.getId(), Constants.INVALID);
            List<QuestionItemDetailsDTO> questionItemDetailsDTOS = questionItemDetailsMapper.questionItemDetailsToQuestionItemDetailsDTOs(questionItemDetailss);
            questionItemDTO.setQuestionItemDetailsDTOS(questionItemDetailsDTOS);
        }
        return questionItemDTOS;
    }
}
