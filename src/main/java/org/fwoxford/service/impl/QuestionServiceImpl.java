package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.QuestionItemService;
import org.fwoxford.service.QuestionService;
import org.fwoxford.service.dto.QuestionDTO;
import org.fwoxford.service.dto.QuestionItemDTO;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.service.dto.ReplyDetailsDTO;
import org.fwoxford.service.dto.response.QuestionForDataTableEntity;
import org.fwoxford.service.mapper.QuestionMapper;
import org.fwoxford.service.mapper.ReplyDetailsMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service Implementation for managing Question.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;
    @Autowired
    QuestionRepositories questionRepositories;
    @Autowired
    BankUtil bankUtil;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    DelegateRepository delegateRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionItemService questionItemService;

    @Autowired
    SendRecordRepository sendRecordRepository;

    @Autowired
    ReplyRecordRepository replyRecordRepository;

    @Autowired
    ReplyDetailsRepository replyDetailsRepository;
    @Autowired
    ReplyDetailsMapper replyDetailsMapper;
    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);

        //生成问题编码
        String questionCode = "";
        if(questionDTO.getId()==null){
            questionCode = bankUtil.getUniqueIDByDate(Constants.QUESTION_CODE,new Date());
        }else{
            Question questionOld = questionRepository.findOne(questionDTO.getId());
            if(questionOld!=null && !questionOld.getStatus().equals(Constants.QUESTION_IN_DRAFT)){
                throw new BankServiceException("问题已不在草拟中，不能编码！");
            }
            questionCode = questionOld.getQuestionCode();
        }
        questionDTO.setQuestionCode(questionCode);
        //项目
        if(questionDTO.getProjectId()!=null){
            Project project = projectRepository.findByIdAndStatus(questionDTO.getProjectId(), Constants.VALID);
            if(project == null){
                throw new BankServiceException("项目不存在！");
            }
            questionDTO.setProjectName(project.getProjectName());
            questionDTO.setProjectCode(project.getProjectCode());
        }
        //相关单位（委托方）
       if(questionDTO.getRelatedAgencyId()!=null){
           Delegate delegate = delegateRepository.findByIdAndStatus(questionDTO.getRelatedAgencyId(), Constants.VALID);
           if(delegate == null){
               throw new BankServiceException("相关单位不存在！");
           }
           questionDTO.setRelatedAgency(delegate.getDelegateName());
       }
       //提问人
        if(questionDTO.getAuthorId()!=null){
           User user = userRepository.findOne(questionDTO.getAuthorId());
           if(user ==null){
               throw new BankServiceException("提问人不存在！");
           }
            questionDTO.setAuthor(user.getLastName()+user.getFirstName());
        }
        questionDTO.setStatus(Constants.QUESTION_IN_DRAFT);
        Question question = questionMapper.questionDTOToQuestion(questionDTO);
        question = questionRepository.save(question);
        questionDTO.setId(question.getId());
        return questionDTO;
    }

    /**
     *  Get all the questions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        Page<Question> result = questionRepository.findAll(pageable);
        return result.map(question -> questionMapper.questionToQuestionDTO(question));
    }

    /**
     *  Get one question by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QuestionDTO findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        Question question = questionRepository.findOne(id);
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(question);
        return questionDTO;
    }

    /**
     *  Delete the  question by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.delete(id);
    }

    /**
     * 分页查询问题清单
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<QuestionForDataTableEntity> findPageQuestions(DataTablesInput input) {
        DataTablesOutput<QuestionForDataTableEntity> transhipDataTablesOutput =  questionRepositories.findAll(input);
        return transhipDataTablesOutput;
    }

    /**
     * 保存问题，问题ITEM，问题ITEM 详情
     * @param questionDTO
     * @return
     */
    @Override
    public QuestionDTO saveQuestionAndItemsAndDetails(QuestionDTO questionDTO) {
        //保存问题
        questionDTO = this.save(questionDTO) ;
        //保存问题ITEM
        List<QuestionItemDTO> questionItemDTOS = questionDTO.getQuestionItemDTOList();
        if(questionItemDTOS==null){
            return questionDTO;
        }
        for(QuestionItemDTO questionItemDTO : questionDTO.getQuestionItemDTOList()){
            questionItemDTO.setQuestionId(questionDTO.getId());
            questionItemDTO = questionItemService.save(questionItemDTO);
        }
        return questionDTO;
    }

    /**
     * 根据问题ID查询问题详情以及ITEM LIST , ITEM DETAILS
     * @param id
     * @return
     */
    @Override
    public QuestionDTO findQuestionAndItemsAndDetails(Long id) {
        Question question = questionRepository.findOne(id);
        if(question == null){
            throw new BankServiceException("问题不存在！",id.toString());
        }
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(question);
        List<QuestionItemDTO> questionItemDTOs = questionItemService.findQuestionItemAndDetailsByQuestionId(id);
        questionDTO.setQuestionItemDTOList(questionItemDTOs);
        return questionDTO;
    }

    /**
     * 陌生人查询问题详情以及回复内容列表
     * @param id
     * @return
     */
    @Override
    public QuestionDTO findReplyRecordAndQuestionDetails(Long id) {
        SendRecord sendRecord = sendRecordRepository.findOne(id);
        if(sendRecord == null){
            throw new BankServiceException("未查询到发送记录！");
        }
        Long questionId = sendRecord.getQuestionId();
        QuestionDTO questionDTO = findQuestionAndItemsAndDetails(questionId);

        ReplyRecord replyRecord = replyRecordRepository.findBySendRecordId(id);
        if(replyRecord == null){
            throw new BankServiceException("未查询到回复记录！");
        }
        //查询回复详情
        List<ReplyDetails> replyDetailss = replyDetailsRepository.findByReplyRecordId(replyRecord.getId());
        List<QuestionItemDTO> questionItemDTOS = questionDTO.getQuestionItemDTOList();

        questionItemDTOS.forEach(s->{
            List<QuestionItemDetailsDTO> questionItemDetailss = s.getQuestionItemDetailsDTOS();
            List<ReplyDetailsDTO> replyDetailsDTOS = replyDetailsMapper.questionItemDetailsToReplyDetailsDTOs(questionItemDetailss,replyDetailss);
            s.setReplyDetailsDTOS(replyDetailsDTOS);
            s.setQuestionItemDetailsDTOS(null);
        });
        return questionDTO;
    }
}
