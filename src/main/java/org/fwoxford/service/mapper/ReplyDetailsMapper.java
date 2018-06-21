package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ReplyDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReplyDetails and its DTO ReplyDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {ReplyRecordMapper.class, QuestionItemDetailsMapper.class})
public interface ReplyDetailsMapper extends EntityMapper<ReplyDetailsDTO, ReplyDetails> {

    @Mapping(source = "replyRecord.id", target = "replyRecordId")
    @Mapping(source = "questionItemDetails.id", target = "questionItemDetailsId")
    ReplyDetailsDTO toDto(ReplyDetails replyDetails);

    @Mapping(source = "replyRecordId", target = "replyRecord")
    @Mapping(source = "questionItemDetailsId", target = "questionItemDetails")
    ReplyDetails toEntity(ReplyDetailsDTO replyDetailsDTO);

    default ReplyDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReplyDetails replyDetails = new ReplyDetails();
        replyDetails.setId(id);
        return replyDetails;
    }
    default ReplyRecord replyFromId(Long id) {
        if (id == null) {
            return null;
        }
        ReplyRecord replyRecord = new ReplyRecord();
        replyRecord.setId(id);
        return replyRecord;
    }
    default QuestionItemDetails questionItemDetailsFromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestionItemDetails questionItemDetails = new QuestionItemDetails();
        questionItemDetails.setId(id);
        return questionItemDetails;
    }
}
