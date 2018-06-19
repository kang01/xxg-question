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

    @Mapping(source = "questionItemDetails.id", target = "questionItemDetailsId")
    ReplyRecordDTO replyRecordToRreplyRecordDTO(ReplyRecord replyRecord);

    List<ReplyRecordDTO> replyRecordsToReplyRecordDTOs(List<ReplyRecord> replyRecordList);

    @Mapping(source = "questionItemDetailsId", target = "questionItemDetails")
    ReplyRecord replyRecordDTOToRreplyRecord(ReplyRecordDTO replyRecordDTO);

    List<ReplyRecord> replyRecordDTOsToReplyRecords(List<ReplyRecordDTO> replyRecordDTOList);

    default QuestionItemDetails questionItemDetailsFromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestionItemDetails questionItemDetails = new QuestionItemDetails();
        questionItemDetails.setId(id);
        return questionItemDetails;
    }
}
