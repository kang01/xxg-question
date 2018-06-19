package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.AuthorizationRecordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AuthorizationRecord and its DTO AuthorizationRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorizationRecordMapper extends EntityMapper<AuthorizationRecordDTO, AuthorizationRecord> {



    default AuthorizationRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        AuthorizationRecord authorizationRecord = new AuthorizationRecord();
        authorizationRecord.setId(id);
        return authorizationRecord;
    }
}
