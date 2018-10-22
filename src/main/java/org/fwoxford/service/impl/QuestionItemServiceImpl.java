package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.FrozenTubeRepository;
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
import java.util.stream.Collectors;

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
    @Autowired
    FrozenTubeRepository frozenTubeRepository;

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
        List<FrozenTube> frozenTubeListForUnLock = new ArrayList<>();
        if(questionItemDTO.getId()!=null){
            List<QuestionItemDetails> questionItemDetailssOld = questionItemDetailsRepository.findByQuestionItemIdAndStatusNot(
                questionItemDTO.getId(), Constants.INVALID);
            questionItemDetailssOld.forEach(s->{
                if(!questionItemDTO.getFrozenTubeIds().contains(s.getFrozenTube().getId())){
                    s.setStatus(Constants.INVALID);
                    questionItemDetailssForDel.add(s);
                    FrozenTube frozenTube= s.getFrozenTube();
                    frozenTube.lockFlag(Constants.LOCK_FLAG_NO).bussinessType(null).bussinessId(null);
                    frozenTubeListForUnLock.add(frozenTube);
                }else{
                    questionItemDetailssForSave.add(s);
                }
            });
        }
        frozenTubeRepository.save(frozenTubeListForUnLock);
        questionItemDetailsRepository.save(questionItemDetailssForDel);

        //需要验证样本是否被锁住以及样本的状态：条件是，样本未被锁住，且状态为已入库或者接收完成
        List<Long> frozenTubeIds = questionItemDTO.getFrozenTubeIds();
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findByIdIn(frozenTubeIds);
        for(Long frozenTubeId :questionItemDTO.getFrozenTubeIds()){
            FrozenTube frozenTube = frozenTubeList.stream().filter(s->s.getId().equals(frozenTubeId)).findFirst().orElse(null);
            if(frozenTube == null){
                throw new BankServiceException("ID为"+frozenTubeId+"的样本不存在！");
            }
            QuestionItemDetails questionItemDetails = questionItemDetailssForSave.stream().filter(s->s.getFrozenTube().getId().equals(frozenTubeId)).findFirst().orElse(null);
            if(questionItemDetails == null){
                if(!frozenTube.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKED)&& !frozenTube.getFrozenTubeState().equals(Constants.FROZEN_BOX_TRANSHIP_COMPLETE)
                    && !frozenTube.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKING)){
                    throw new BankServiceException("样本未入库或未接收完成，不能创建问题！");
                }
                if(frozenTube.getLockFlag()!=null && frozenTube.getLockFlag().equals(Constants.LOCK_FLAG_YES)){
                    throw new BankServiceException("样本被锁于其他业务中，不能创建问题！");
                }
                frozenTube.lockFlag(Constants.LOCK_FLAG_YES).bussinessId(questionItem.getId()).bussinessType(Constants.BUSSINESS_TYPE_QUESTION);
                questionItemDetails = new QuestionItemDetails().questionItem(questionItem).frozenTube(frozenTube).status(Constants.VALID);
            }
            questionItemDetailssForSave.add(questionItemDetails);
        }
        questionItemDetailsRepository.save(questionItemDetailssForSave);
        frozenTubeRepository.save(frozenTubeList);
        questionItemDTO.setQuestionItemDetailsDTOS(questionItemDetailsMapper.questionItemDetailsToQuestionItemDetailsDTOs(questionItemDetailssForSave));
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
        List<QuestionItemDetails> questionItemDetailss = questionItemDetailsRepository.findByQuestionItemIdAndStatusNot(id,Constants.INVALID);
        List<FrozenTube> frozenTubeListUnLock = new ArrayList<>();
        questionItemDetailss.forEach(s->{
            s.status(Constants.INVALID);
            FrozenTube frozenTube = s.getFrozenTube();
            frozenTube.bussinessType(null).bussinessId(null).lockFlag(Constants.LOCK_FLAG_NO);
            frozenTubeListUnLock.add(frozenTube);
        });
        frozenTubeRepository.save(frozenTubeListUnLock);
        questionItemDetailsRepository.save(questionItemDetailss);
        questionItemRepository.save(questionItem);
//        questionItemDetailsRepository.updateStatusByQuestionItemquestionItemId(id);
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
            //查询样本的来源
            List<Long> frozenTubeIds = questionItemDetailsDTOS.stream().map(s->s.getFrozenTubeId()).collect(Collectors.toList());
            List<List<Long>> frozenTubeIdEach1000 = Lists.partition(frozenTubeIds,1000);
            List<Object[]> samplesMsg = new ArrayList<>();
            for(List<Long> ids :frozenTubeIdEach1000){
                //查询样本首次接收记录
                List<Object[]> transhipTubesMsg = questionItemDetailsRepository.findByFrozenTubeIdsIn(ids);
                samplesMsg.addAll(transhipTubesMsg);
            }
            questionItemDetailsDTOS.forEach(s->{
                Object[] obj = samplesMsg.stream().filter(t->t[0]!=null && Long.valueOf(t[0].toString()).equals(s.getFrozenTubeId())).findFirst().orElse(null);
                if(obj!=null){
                    s.setReceiveBoxCode(obj[1]!=null?obj[1].toString():null);
                    s.setFrozenBoxCode1D(obj[2]!=null?obj[2].toString():null);
                }
            });
            questionItemDTO.setQuestionItemDetailsDTOS(questionItemDetailsDTOS);
        }
        return questionItemDTOS;
    }
}
