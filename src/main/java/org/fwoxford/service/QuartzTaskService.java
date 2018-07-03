package org.fwoxford.service;

import org.fwoxford.service.dto.QuartzTaskDTO;
import org.fwoxford.service.dto.SendRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing QuartzTask.
 */
public interface QuartzTaskService {

    /**
     * Save a quartsTask.
     *
     * @param quartzTaskDTO the entity to save
     * @return the persisted entity
     */
    QuartzTaskDTO save(QuartzTaskDTO quartzTaskDTO);

    /**
     * Get all the quartsTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuartzTaskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" quartsTask.
     *
     * @param id the id of the entity
     * @return the entity
     */
    QuartzTaskDTO findOne(Long id);

    /**
     * Delete the "id" quartsTask.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 获取所有启用的定时任务
     * @return
     */
    List<QuartzTaskDTO> findAllValidTask();

    /**
     * 创建定时任务
     * @param sendRecords
     */
    void createQuartzTaskForNotice(List<SendRecordDTO> sendRecords);

    void createQuartzTaskForDelayCheck(List<SendRecordDTO> sendRecords);

    /**
     * 修改触发时间
     * @param quartzTaskDTO
     */
    void modifyTriggerTime(QuartzTaskDTO quartzTaskDTO);
}
