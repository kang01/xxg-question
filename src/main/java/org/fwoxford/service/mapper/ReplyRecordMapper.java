package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ReplyRecordDTO;

import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for the entity ReplyRecord and its DTO ReplyRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface  ReplyRecordMapper {

    ReplyRecordDTO replyRecordToRreplyRecordDTO(ReplyRecord replyRecord);

    List<ReplyRecordDTO> replyRecordsToReplyRecordDTOs(List<ReplyRecord> replyRecordList);

    ReplyRecord replyRecordDTOToReplyRecord(ReplyRecordDTO replyRecordDTO);

    List<ReplyRecord> replyRecordDTOsToReplyRecords(List<ReplyRecordDTO> replyRecordDTOList);

}
