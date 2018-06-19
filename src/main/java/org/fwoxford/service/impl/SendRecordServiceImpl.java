package org.fwoxford.service.impl;

import org.fwoxford.service.SendRecordService;
import org.fwoxford.domain.SendRecord;
import org.fwoxford.repository.SendRecordRepository;
import org.fwoxford.service.dto.SendRecordDTO;
import org.fwoxford.service.mapper.SendRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing SendRecord.
 */
@Service
@Transactional
public class SendRecordServiceImpl implements SendRecordService {

    private final Logger log = LoggerFactory.getLogger(SendRecordServiceImpl.class);

    private final SendRecordRepository sendRecordRepository;

    private final SendRecordMapper sendRecordMapper;

    public SendRecordServiceImpl(SendRecordRepository sendRecordRepository, SendRecordMapper sendRecordMapper) {
        this.sendRecordRepository = sendRecordRepository;
        this.sendRecordMapper = sendRecordMapper;
    }

    /**
     * Save a sendRecord.
     *
     * @param sendRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SendRecordDTO save(SendRecordDTO sendRecordDTO) {
        log.debug("Request to save SendRecord : {}", sendRecordDTO);
        SendRecord sendRecord = sendRecordMapper.toEntity(sendRecordDTO);
        sendRecord = sendRecordRepository.save(sendRecord);
        return sendRecordMapper.toDto(sendRecord);
    }

    /**
     * Get all the sendRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SendRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SendRecords");
        return sendRecordRepository.findAll(pageable)
            .map(sendRecordMapper::toDto);
    }

    /**
     * Get one sendRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SendRecordDTO findOne(Long id) {
        log.debug("Request to get SendRecord : {}", id);
        SendRecord sendRecord = sendRecordRepository.findOne(id);
        return sendRecordMapper.toDto(sendRecord);
    }

    /**
     * Delete the sendRecord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SendRecord : {}", id);
        sendRecordRepository.delete(id);
    }
}
