package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.service.dto.ReplyDetailsDTO;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

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

    default List<ReplyDetailsDTO> questionItemDetailsToReplyDetailsDTOs(List<QuestionItemDetailsDTO> questionItemDetailss, List<ReplyDetails> replyDetailss){
        List<ReplyDetailsDTO> replyDetailsDTOS = new ArrayList<>();
        questionItemDetailss.forEach(s->{
            ReplyDetailsDTO replyDetailsDTO = questionItemDetailToReplyDetailsDTO(s,replyDetailss);
            replyDetailsDTOS.add(replyDetailsDTO);
        });
        return replyDetailsDTOS;
    }

    default ReplyDetailsDTO questionItemDetailToReplyDetailsDTO(QuestionItemDetailsDTO s, List<ReplyDetails> replyDetailss){
        ReplyDetailsDTO replyDetailsDTO = new ReplyDetailsDTO();
        ReplyDetails replyDetails = replyDetailss.stream().filter(r->r.getQuestionItemDetails().getId().equals(s.getId())).findFirst().orElse(null);
        if(replyDetails!=null){
            replyDetailsDTO = toDto(replyDetails);
        }
        replyDetailsDTO = questionItemDetailToReplyQuestionFrozenTube(replyDetailsDTO,s);
        return replyDetailsDTO;
    }

    default ReplyDetailsDTO questionItemDetailToReplyQuestionFrozenTube(ReplyDetailsDTO replyDetailsDTO ,QuestionItemDetailsDTO s){
        if(replyDetailsDTO == null){
            replyDetailsDTO = new ReplyDetailsDTO();
        }
        replyDetailsDTO.setSampleCode(s.getSampleCode());
        replyDetailsDTO.setSampleTypeId(s.getSampleTypeId());
        replyDetailsDTO.setSampleTypeCode(s.getSampleTypeCode());
        replyDetailsDTO.setSampleTypeName(s.getSampleTypeName());
        replyDetailsDTO.setSampleClassificationId(s.getSampleClassificationId());
        replyDetailsDTO.setSampleClassificationCode(s.getSampleClassificationCode());
        replyDetailsDTO.setSampleClassificationName(s.getSampleClassificationName());
        replyDetailsDTO.setProjectId(s.getProjectId());
        replyDetailsDTO.setProjectCode(s.getProjectCode());
        replyDetailsDTO.setPatientId(s.getPatientId());
        replyDetailsDTO.setFrozenBoxCode(s.getFrozenBoxCode());
        replyDetailsDTO.setFrozenBoxId(s.getFrozenBoxId());
        replyDetailsDTO.setFrozenBoxCode1D(s.getFrozenBoxCode1D());
        replyDetailsDTO.setPosition(s.getPosition());
        replyDetailsDTO.setStatus(s.getStatus());
        replyDetailsDTO.setQuestionItemDetailsId(s.getId());
        replyDetailsDTO.setTag1(s.getTag1());
        replyDetailsDTO.setTag2(s.getTag2());
        replyDetailsDTO.setTag3(s.getTag3());
        replyDetailsDTO.setTag4(s.getTag4());
        replyDetailsDTO.setTubeRows(s.getTubeRows());
        replyDetailsDTO.setTubeColumns(s.getTubeColumns());
        return replyDetailsDTO;
    }
}
