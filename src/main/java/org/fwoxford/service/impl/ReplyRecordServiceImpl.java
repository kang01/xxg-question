package org.fwoxford.service.impl;

import org.fwoxford.service.ReplyRecordService;
import org.fwoxford.domain.ReplyRecord;
import org.fwoxford.repository.ReplyRecordRepository;
import org.fwoxford.service.dto.ReplyRecordDTO;
import org.fwoxford.service.mapper.ReplyRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ReplyRecord.
 */
@Service
@Transactional
public class ReplyRecordServiceImpl implements ReplyRecordService {

    private final Logger log = LoggerFactory.getLogger(ReplyRecordServiceImpl.class);

    private final ReplyRecordRepository replyRecordRepository;

    private final ReplyRecordMapper replyRecordMapper;

    public ReplyRecordServiceImpl(ReplyRecordRepository replyRecordRepository, ReplyRecordMapper replyRecordMapper) {
        this.replyRecordRepository = replyRecordRepository;
        this.replyRecordMapper = replyRecordMapper;
    }

    /**
     * Save a replyRecord.
     *
     * @param replyRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReplyRecordDTO save(ReplyRecordDTO replyRecordDTO) {
        log.debug("Request to save ReplyRecord : {}", replyRecordDTO);
        ReplyRecord replyRecord = replyRecordMapper.replyRecordDTOToRreplyRecord(replyRecordDTO);
        replyRecord = replyRecordRepository.save(replyRecord);
        return replyRecordMapper.replyRecordToRreplyRecordDTO(replyRecord);
    }

    /**
     * Get all the replyRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReplyRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReplyRecords");
        return replyRecordRepository.findAll(pageable)
            .map(replyRecordMapper::replyRecordToRreplyRecordDTO);
    }

    /**
     * Get one replyRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ReplyRecordDTO findOne(Long id) {
        log.debug("Request to get ReplyRecord : {}", id);
        ReplyRecord replyRecord = replyRecordRepository.findOne(id);
        return replyRecordMapper.replyRecordToRreplyRecordDTO(replyRecord);
    }

    /**
     * Delete the replyRecord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReplyRecord : {}", id);
        replyRecordRepository.delete(id);
    }
}
