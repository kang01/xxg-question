package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.config.QuartzManager;
import org.fwoxford.domain.QuartzTask;
import org.fwoxford.service.QuartzTaskService;
import org.fwoxford.repository.QuartzTaskRepository;
import org.fwoxford.service.dto.AuthorizationRecordDTO;
import org.fwoxford.service.dto.QuartzTaskDTO;
import org.fwoxford.service.dto.SendRecordDTO;
import org.fwoxford.service.mapper.QuartzTaskMapper;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Service Implementation for managing QuartzTask.
 */
@Service
@Transactional
public class QuartzTaskServiceImpl implements QuartzTaskService {

    private final Logger log = LoggerFactory.getLogger(QuartzTaskServiceImpl.class);

    private final QuartzTaskRepository quartzTaskRepository;

    private final QuartzTaskMapper quartzTaskMapper;
    @Autowired
    QuartzManager quartzManager;

    public QuartzTaskServiceImpl(QuartzTaskRepository quartzTaskRepository, QuartzTaskMapper quartzTaskMapper) {
        this.quartzTaskRepository = quartzTaskRepository;
        this.quartzTaskMapper = quartzTaskMapper;
    }

    /**
     * Save a quartsTask.
     *
     * @param quartzTaskDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuartzTaskDTO save(QuartzTaskDTO quartzTaskDTO) {
        log.debug("Request to save QuartzTask : {}", quartzTaskDTO);
        QuartzTask quartzTask = quartzTaskMapper.toEntity(quartzTaskDTO);
        quartzTask = quartzTaskRepository.save(quartzTask);
        return quartzTaskMapper.toDto(quartzTask);
    }

    /**
     * Get all the quartsTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuartzTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuartsTasks");
        return quartzTaskRepository.findAll(pageable)
            .map(quartzTaskMapper::toDto);
    }

    /**
     * Get one quartsTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QuartzTaskDTO findOne(Long id) {
        log.debug("Request to get QuartzTask : {}", id);
        QuartzTask quartzTask = quartzTaskRepository.findOne(id);
        return quartzTaskMapper.toDto(quartzTask);
    }

    /**
     * Delete the quartsTask by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuartzTask : {}", id);
        quartzTaskRepository.delete(id);
    }

    @Override
    public List<QuartzTaskDTO> findAllValidTask() {
        return quartzTaskMapper.toDto(quartzTaskRepository.findByEnableStatus(Constants.TASK_ENABLE_STATUS_YES));
    }

    /**
     * 保存定时任务同时创建定时任务---到期提醒
     * @param sendRecords
     */
    @Override
    public void createQuartzTaskForNotice(List<SendRecordDTO> sendRecords) {
        List<QuartzTask> quartzTasks = new ArrayList<>();
        //创建过期提醒任务
        for(SendRecordDTO sendRecord :sendRecords){
            String uuid = UUID.randomUUID().toString();
            //根据到期时间,减一小时为提醒时间
            //到期时间为已过期任务更改时间
            ZonedDateTime zonedDateTime = sendRecord.getExpirationTime();
            //到期提醒时间
            ZonedDateTime noticeTime = zonedDateTime.minusHours(1L);
            Date date = Date.from(noticeTime.toInstant());
            String cronTime = BankUtil.getCron(date);
            QuartzTask quartzTask = new QuartzTask();
            //businessId 为授权ID
            quartzTask.status(Constants.VALID).className(Constants.CLASS_NAME_TASK_MESSAGE).enableStatus(Constants.TASK_ENABLE_STATUS_YES)
                    .jobName("MESSAGE_JOB_"+uuid).jobGroup("MESSAGE").triggerName("MESSAGE_TRIGGER_"+uuid).triggerGroup("MESSAGE_TRIGGER")
                    .triggerTime(cronTime).businessId(sendRecord.getAuthorizationRecordId());
            quartzTasks.add(quartzTask);
        }
        quartzTaskRepository.save(quartzTasks);
        quartzManager.createJob(quartzTaskMapper.toDto(quartzTasks));
    }

    @Override
    public void createQuartzTaskForDelayCheck(List<SendRecordDTO> sendRecords) {
        List<QuartzTask> quartzTasks = new ArrayList<>();
        //创建未回复更改为已过期的任务
        for(SendRecordDTO sendRecord :sendRecords){
            String uuid = UUID.randomUUID().toString();
            //到期时间为已过期任务更改时间
            ZonedDateTime zonedDateTime = sendRecord.getExpirationTime();
            //过期任务更改时间
            Date delayDate = Date.from(zonedDateTime.toInstant());
            String delayTime = BankUtil.getCron(delayDate);
            QuartzTask quartzTask = new QuartzTask();
            //businessId 为发送ID
            quartzTask.status(Constants.VALID).className(Constants.CLASS_NAME_TASK_REPLY).enableStatus(Constants.TASK_ENABLE_STATUS_YES)
                    .jobName("DELAY_JOB_"+uuid).jobGroup("DELAY").triggerName("DELAY_TRIGGER_"+uuid).triggerGroup("DELAY_TRIGGER")
                    .triggerTime(delayTime).businessId(sendRecord.getId());
            quartzTasks.add(quartzTask);
        }
        quartzTaskRepository.save(quartzTasks);
    }
}
