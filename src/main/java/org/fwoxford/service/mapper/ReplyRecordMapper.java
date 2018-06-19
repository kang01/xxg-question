package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ReplyRecordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReplyRecord and its DTO ReplyRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {QuestionItemDetailsMapper.class})
public interface ReplyRecordMapper extends EntityMapper<ReplyRecordDTO, ReplyRecord> {

//    @Mapping(source = "questionItemDetails.id", target = "questionItemDetailsId")
    ReplyRecordDTO toDto(ReplyRecord replyRecord);

//    @Mapping(source = "questionItemDetailsId", target = "questionItemDetails")
    ReplyRecord toEntity(ReplyRecordDTO replyRecordDTO);

    default ReplyRecord replyRecordFromId(Long id) {
        if (id == null) {
            return null;
        }
        ReplyRecord replyRecord = new ReplyRecord();
        replyRecord.setId(id);
        return replyRecord;
    }
}
