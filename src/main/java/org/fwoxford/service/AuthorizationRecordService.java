package org.fwoxford.service;

import org.fwoxford.domain.SendRecord;
import org.fwoxford.service.dto.AuthorizationRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing AuthorizationRecord.
 */
public interface AuthorizationRecordService {

    /**
     * Save a authorizationRecord.
     *
     * @param authorizationRecordDTO the entity to save
     * @return the persisted entity
     */
    AuthorizationRecordDTO save(AuthorizationRecordDTO authorizationRecordDTO);

    /**
     * Get all the authorizationRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AuthorizationRecordDTO> findAll(Pageable pageable);

    /**
     * Get the "id" authorizationRecord.
     *
     * @param id the id of the entity
     * @return the entity
     */
    AuthorizationRecordDTO findOne(Long id);

    /**
     * Delete the "id" authorizationRecord.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 保存授权码
     * @param authorizationRecordDTOs
     * @return
     */
    List<AuthorizationRecordDTO> saveAuthorizationRecords(Long questionId, List<AuthorizationRecordDTO> authorizationRecordDTOs);

    /**
     *
     * @param id
     * @return
     */
    List<AuthorizationRecordDTO> findAllAuthorizationRecordsByQuestionId(Long id);
}
