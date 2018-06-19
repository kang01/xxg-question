package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.AuthorizationRecordDTO;

import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for the entity AuthorizationRecord and its DTO AuthorizationRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorizationRecordMapper {

    AuthorizationRecordDTO authorizationRecordToAuthorizationRecordDTO(AuthorizationRecord authorizationRecord);

    List<AuthorizationRecordDTO> authorizationRecordsToAuthorizationRecordDTOs(List<AuthorizationRecord> authorizationRecords);

    AuthorizationRecord authorizationRecordDTOToAuthorizationRecord(AuthorizationRecordDTO authorizationRecordDTO);

    List<AuthorizationRecord> authorizationRecordDTOsToAuthorizationRecords(List<AuthorizationRecordDTO> authorizationRecordDTOS);

    default AuthorizationRecord authorizationRecordFromId(Long id) {
        if (id == null) {
            return null;
        }
        AuthorizationRecord authorizationRecord = new AuthorizationRecord();
        authorizationRecord.setId(id);
        return authorizationRecord;
    }
}
