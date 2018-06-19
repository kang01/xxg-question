package org.fwoxford.service.impl;

import org.fwoxford.service.AuthorizationRecordService;
import org.fwoxford.domain.AuthorizationRecord;
import org.fwoxford.repository.AuthorizationRecordRepository;
import org.fwoxford.service.dto.AuthorizationRecordDTO;
import org.fwoxford.service.mapper.AuthorizationRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing AuthorizationRecord.
 */
@Service
@Transactional
public class AuthorizationRecordServiceImpl implements AuthorizationRecordService {

    private final Logger log = LoggerFactory.getLogger(AuthorizationRecordServiceImpl.class);

    private final AuthorizationRecordRepository authorizationRecordRepository;

    private final AuthorizationRecordMapper authorizationRecordMapper;

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
        AuthorizationRecord authorizationRecord = authorizationRecordMapper.toEntity(authorizationRecordDTO);
        authorizationRecord = authorizationRecordRepository.save(authorizationRecord);
        return authorizationRecordMapper.toDto(authorizationRecord);
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
            .map(authorizationRecordMapper::toDto);
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
        return authorizationRecordMapper.toDto(authorizationRecord);
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
}
