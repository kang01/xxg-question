package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.Question;
import org.fwoxford.domain.User;
import org.fwoxford.repository.QuestionRepository;
import org.fwoxford.repository.UserRepository;
import org.fwoxford.security.SecurityUtils;
import org.fwoxford.service.AuthorizationRecordService;
import org.fwoxford.domain.AuthorizationRecord;
import org.fwoxford.repository.AuthorizationRecordRepository;
import org.fwoxford.service.SendRecordService;
import org.fwoxford.service.dto.AuthorizationRecordDTO;
import org.fwoxford.service.mapper.AuthorizationRecordMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing AuthorizationRecord.
 */
@Service
@Transactional
public class AuthorizationRecordServiceImpl implements AuthorizationRecordService {

    private final Logger log = LoggerFactory.getLogger(AuthorizationRecordServiceImpl.class);

    private final AuthorizationRecordRepository authorizationRecordRepository;

    private final AuthorizationRecordMapper authorizationRecordMapper;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SendRecordService sendRecordService;
    @Autowired
    UserRepository userRepository;

    public AuthorizationRecordServiceImpl(AuthorizationRecordRepository authorizationRecordRepository, AuthorizationRecordMapper authorizationRecordMapper) {
        this.authorizationRecordRepository = authorizationRecordRepository;
        this.authorizationRecordMapper = authorizationRecordMapper;
    }

    /**
     * Save a authorizationRecord.
     *
     * @param authorizationRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AuthorizationRecordDTO save(AuthorizationRecordDTO authorizationRecordDTO) {
        log.debug("Request to save AuthorizationRecord : {}", authorizationRecordDTO);
        AuthorizationRecord authorizationRecord = authorizationRecordMapper.authorizationRecordDTOToAuthorizationRecord(authorizationRecordDTO);
        authorizationRecord = authorizationRecordRepository.save(authorizationRecord);
        return authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);
    }

    /**
     * Get all the authorizationRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AuthorizationRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AuthorizationRecords");
        return authorizationRecordRepository.findAll(pageable)
            .map(authorizationRecordMapper::authorizationRecordToAuthorizationRecordDTO);
    }

    /**
     * Get one authorizationRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AuthorizationRecordDTO findOne(Long id) {
        log.debug("Request to get AuthorizationRecord : {}", id);
        AuthorizationRecord authorizationRecord = authorizationRecordRepository.findOne(id);
        return authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);
    }

    /**
     * Delete the authorizationRecord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AuthorizationRecord : {}", id);
        authorizationRecordRepository.delete(id);
    }

    /**
     * 保存授权码
     * @param questionId
     * @param authorizationRecordDTOs
     * @return
     */
    @Override
    public List<AuthorizationRecordDTO> saveAuthorizationRecords(Long questionId, List<AuthorizationRecordDTO> authorizationRecordDTOs) {
        //验证传递参数中邮箱是否有重复项
        Map<String,List<AuthorizationRecordDTO>> mapGroupByEmail = authorizationRecordDTOs.stream().collect(Collectors.groupingBy(s->s.getStrangerEmail()));
        for(List<AuthorizationRecordDTO> values :mapGroupByEmail.values()){
            if(values.size()>1){
                throw new BankServiceException("请勿提交重复的邮箱！",values.get(0).getStrangerEmail());
            }
        }
        if(questionId == null){
            throw new BankServiceException("问题ID不能为空！");
        }
        Question question = questionRepository.findOne(questionId);
        //判断每一个人的邮箱是否有发送过，每一个人在有效期之内，只能发送一次
        if(question == null || (question!=null &&! question.getStatus().equals(Constants.QUESTION_IN_DRAFT)&&! question.getStatus().equals(Constants.QUESTION_ASKED))){
            throw new BankServiceException("问题不在草拟中，不能编辑授权信息！");
        }
        String username = SecurityUtils.getCurrentUserLogin().get();
        Long authorizationPersonId = null;
        if(!StringUtils.isEmpty(username)){
            User user = userRepository.findByLogin(username);
            authorizationPersonId = user!=null?user.getId():3L;
        }
        List<Long> oldIds = authorizationRecordDTOs.stream().map(s->
            {
                Long id = -1L;
                if(s.getId()!=null){
                    id = s.getId();
                }
                return id;
            }
        ).collect(Collectors.toList());
        List<AuthorizationRecord> authorizationRecords = authorizationRecordRepository.findByQuestionIdAndStatusNot(questionId, Constants.INVALID);
        List<AuthorizationRecord> authorizationRecordsForDelete = new ArrayList<>();
        authorizationRecords.forEach(s->{
            if(s.getId()!=null && !oldIds.contains(s.getId())){
                s.status(Constants.INVALID);
                authorizationRecordsForDelete.add(s);
            }
        });
        //删除无效的
        authorizationRecordRepository.save(authorizationRecordsForDelete);
        //保存有效的
        List<AuthorizationRecord> authorizationRecordsForSave = new ArrayList<>();

        for(AuthorizationRecordDTO dto : authorizationRecordDTOs ){
            AuthorizationRecord authorizationRecord = authorizationRecordMapper.authorizationRecordDTOToAuthorizationRecord(dto);

            authorizationRecord.questionId(questionId).applyTimes(0).authorityName(Constants.AUTHORITY_ROLE_STRANGER+";")
                .expirationTime(Constants.EXPRIATIONTIME).authorityPersonId(authorizationPersonId).questionCode(question.getQuestionCode())
                .status(Constants.VALID);
            authorizationRecordsForSave.add(authorizationRecord);
        }

        authorizationRecordRepository.save(authorizationRecordsForSave);
        sendRecordService.sendEmailRecordToStranger(question,authorizationRecordsForSave);
        question.setStatus(Constants.QUESTION_ASKED);
        questionRepository.save(question);
        return authorizationRecordMapper.authorizationRecordsToAuthorizationRecordDTOs(authorizationRecordsForSave);
    }

    @Override
    public List<AuthorizationRecordDTO> findAllAuthorizationRecordsByQuestionId(Long id) {
        List<AuthorizationRecord> authorizationRecords = authorizationRecordRepository.findByQuestionIdAndStatusNot(id, Constants.INVALID);
        return  authorizationRecordMapper.authorizationRecordsToAuthorizationRecordDTOs(authorizationRecords);
    }
}
