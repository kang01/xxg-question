package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.SendRecordDTO;

import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for the entity SendRecord and its DTO SendRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SendRecordMapper  {

    SendRecordDTO sendRecordToSendRecordDTO(SendRecord sendRecord);

    List<SendRecordDTO> sendRecordsToSendRecordDTOs(List<SendRecord> sendRecords);

    SendRecord sendRecordDTOToSendRecord(SendRecordDTO sendRecordDTO);

    List<SendRecord> sendRecordDTOsToSendRecords(List<SendRecordDTO> sendRecordDTOS);


    default SendRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        SendRecord sendRecord = new SendRecord();
        sendRecord.setId(id);
        return sendRecord;
    }
}
