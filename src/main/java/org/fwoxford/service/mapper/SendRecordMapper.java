package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.SendRecordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SendRecord and its DTO SendRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SendRecordMapper extends EntityMapper<SendRecordDTO, SendRecord> {



    default SendRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        SendRecord sendRecord = new SendRecord();
        sendRecord.setId(id);
        return sendRecord;
    }
}
