package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.Question;
import org.fwoxford.domain.SendRecord;
import org.fwoxford.domain.User;
import org.fwoxford.repository.QuestionRepository;
import org.fwoxford.repository.SendRecordRepository;
import org.fwoxford.repository.UserRepository;
import org.fwoxford.security.SecurityUtils;
import org.fwoxford.service.AuthorizationRecordService;
import org.fwoxford.domain.AuthorizationRecord;
import org.fwoxford.repository.AuthorizationRecordRepository;
import org.fwoxford.service.EurekaApiService;
import org.fwoxford.service.QuartzTaskService;
import org.fwoxford.service.SendRecordService;
import org.fwoxford.service.dto.AuthorizationRecordDTO;
import org.fwoxford.service.dto.SendRecordDTO;
import org.fwoxford.service.mapper.AuthorizationRecordMapper;
import org.fwoxford.service.mapper.SendRecordMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
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
    @Autowired
    QuartzTaskService quartzTaskService;
    @Autowired
    SendRecordMapper sendRecordMapper;
    @Autowired
    SendRecordRepository sendRecordRepository;
    @Autowired
    EurekaApiService eurekaApiService;

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
    public List<AuthorizationRecordDTO> saveAuthorizationRecords(Long questionId, List<AuthorizationRecordDTO> authorizationRecordDTOs) throws UnsupportedEncodingException {
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
        //每次都是发送新数据，不传入原来的邮箱等数据，即只新增，不编辑
        List<AuthorizationRecord> authorizationRecords = authorizationRecordRepository.findByQuestionIdAndStatusNot(questionId, Constants.INVALID);
//        List<AuthorizationRecord> authorizationRecordsForDelete = new ArrayList<>();
//        authorizationRecords.forEach(s->{
//            if(s.getId()!=null && !oldIds.contains(s.getId())){
//                s.status(Constants.INVALID);
//                authorizationRecordsForDelete.add(s);
//            }
//        });
//        //删除无效的
//        authorizationRecordRepository.save(authorizationRecordsForDelete);
        //保存有效的
        List<AuthorizationRecord> authorizationRecordsForSave = new ArrayList<>();

        for(AuthorizationRecordDTO dto : authorizationRecordDTOs ){
            AuthorizationRecord authorizationRecord = authorizationRecordMapper.authorizationRecordDTOToAuthorizationRecord(dto);
            String addr = question.getAuthor()+"|"+question.getQuestionCode()+"|"+dto.getStrangerEmail()+"|"+dto.getStrangerName();
            AuthorizationRecord old = authorizationRecords.stream().filter(s->s.getStrangerEmail().equals(dto.getStrangerEmail())).findFirst().orElse(null);
            if(old!=null){
                throw new BankServiceException("问题"+question.getQuestionCode()+"已发送至邮箱"+dto.getStrangerEmail()+",请勿重复发送！");
            }

            String encryptAddr =  URLEncoder.encode((new BASE64Encoder()).encode(addr.getBytes()),"UTF-8");
            String homeURL = eurekaApiService.queryInstanceHomePageUrl("GWBBISSTRANGERPORTAL");
            String httpUrl = String.format("%s%s%s", homeURL, Constants.STRANGER_HTTP_URL_4_QA, encryptAddr);
            authorizationRecord.questionId(questionId).applyTimes(0).authorityName(Constants.AUTHORITY_ROLE_STRANGER+";").httpUrl(httpUrl)
                .expirationTime(BankUtil.getExpriationTime()).authorityPersonId(authorizationPersonId).questionCode(question.getQuestionCode())
                .status(Constants.VALID);
            authorizationRecordsForSave.add(authorizationRecord);
        }

        authorizationRecordRepository.save(authorizationRecordsForSave);
        List<AuthorizationRecordDTO> authorizationRecordDTOS = authorizationRecordMapper.authorizationRecordsToAuthorizationRecordDTOs(authorizationRecordsForSave);
        List<SendRecordDTO> sendRecords = sendRecordService.sendEmailRecordToStranger(question,authorizationRecordsForSave);
        question.setStatus(Constants.QUESTION_ASKED);
        questionRepository.save(question);
        //创建定时任务--到期提醒
        quartzTaskService.createQuartzTaskForNotice(sendRecords);
        //创建定时任务--过期检查
        quartzTaskService.createQuartzTaskForDelayCheck(sendRecords);
        return authorizationRecordDTOS;
    }

    @Override
    public List<AuthorizationRecordDTO> findAllAuthorizationRecordsByQuestionId(Long id) {
        List<AuthorizationRecord> authorizationRecords = authorizationRecordRepository.findByQuestionIdAndStatusNot(id, Constants.INVALID);
        return  authorizationRecordMapper.authorizationRecordsToAuthorizationRecordDTOs(authorizationRecords);
    }

    /**
     * 根据DTO查询 AuthorizationRecord
     * @param authorizationRecordDTO
     * @return
     */
    @Override
    public AuthorizationRecordDTO findAuthorizationRecordByDTO(AuthorizationRecordDTO authorizationRecordDTO) {
        String strangerMail = authorizationRecordDTO.getStrangerEmail();
        String httpUrl = authorizationRecordDTO.getHttpUrl();
        String questionCode = authorizationRecordDTO.getQuestionCode();
        String authorizationCode = authorizationRecordDTO.getAuthorizationCode();
        if(StringUtils.isEmpty(strangerMail) || StringUtils.isEmpty(httpUrl) || StringUtils.isEmpty(authorizationCode) || StringUtils.isEmpty(questionCode)){
            return new AuthorizationRecordDTO();
        }AuthorizationRecord authorizationRecord = authorizationRecordRepository.findByAuthorizationCodeAndHttpUrlAndStrangerEmailAndQuestionCodeAndStatusNot(authorizationCode,httpUrl,strangerMail,questionCode,Constants.INVALID);
       if(authorizationRecord == null){
            return new AuthorizationRecordDTO();
       }
        //某一个授权信息在一定时间内只有一条数据是进行中的
        SendRecord sendRecord = sendRecordRepository.findByAuthorizationRecordIdAndStatusNotIn(authorizationRecord.getId(),
            new ArrayList<String>(){{add(Constants.INVALID);add(Constants.QUESTION_SEND_RESEND);}});
        if(sendRecord == null){
            return new AuthorizationRecordDTO();
        }
        //找到发送ID 返回
        authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);
        authorizationRecordDTO.setSendRecordId(sendRecord.getId());
        return authorizationRecordDTO;
    }
}
