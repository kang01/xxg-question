package org.fwoxford.service.mapper;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.QuestionItem;
import org.fwoxford.domain.QuestionItemDetails;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity QuestionItemDetails and its DTO QuestionItemDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionItemDetailsMapper  {

    @Mapping(source = "questionItem.id", target = "questionItemId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "frozenTube.sampleCode", target = "sampleCode")
    @Mapping(source = "frozenTube.tubeRows", target = "tubeRows")
    @Mapping(source = "frozenTube.tubeColumns", target = "tubeColumns")
    @Mapping(source = "frozenTube.memo", target = "memo")
    @Mapping(source = "frozenTube.status", target = "status")
    @Mapping(source = "frozenTube.frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "frozenTube.frozenBoxCode", target = "frozenBoxCode")
    @Mapping(source = "frozenTube.frozenBox.frozenBoxCode1D", target = "frozenBoxCode1D")

    @Mapping(source = "frozenTube.sampleType.id", target = "sampleTypeId")
    @Mapping(source = "frozenTube.sampleTypeCode", target = "sampleTypeCode")
    @Mapping(source = "frozenTube.sampleTypeName", target = "sampleTypeName")

    @Mapping(source = "frozenTube.sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "frozenTube.sampleClassificationCode", target = "sampleClassificationCode")
    @Mapping(source = "frozenTube.sampleClassificationName", target = "sampleClassificationName")

    @Mapping(source = "frozenTube.tag1", target = "tag1")
    @Mapping(source = "frozenTube.tag2", target = "tag2")
    @Mapping(source = "frozenTube.tag3", target = "tag3")
    @Mapping(source = "frozenTube.tag4", target = "tag4")
    @Mapping( target = "position", expression ="java(questionItemDetailsFrozenTubePosition(questionItemDetails))")

    @Mapping(source = "frozenTube.project.id", target = "projectId")
    @Mapping(source = "frozenTube.projectCode", target = "projectCode")
    @Mapping(source = "frozenTube.projectSiteCode", target = "projectSiteCode")
    @Mapping(source = "frozenTube.projectSite.projectSiteName", target = "projectSiteName")
    @Mapping(source = "frozenTube.projectSite.id", target = "projectSiteId")
    @Mapping(source = "frozenTube.patientId", target = "patientId")
    QuestionItemDetailsDTO questionItemDetailsToQuestionItemDetailsDTO(QuestionItemDetails questionItemDetails);

    List<QuestionItemDetailsDTO> questionItemDetailsToQuestionItemDetailsDTOs(List<QuestionItemDetails> questionItemDetails);

    @Mapping(source = "questionItemId", target = "questionItem")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    QuestionItemDetails questionItemDetailsDTOToQuestionItemDetails(QuestionItemDetailsDTO questionItemDetailsDTO);

    List<QuestionItemDetails> questionItemDetailsDTOsToQuestionItemDetails(List<QuestionItemDetailsDTO> questionItemDetailsDTOs);

    default String questionItemDetailsFrozenTubePosition(QuestionItemDetails questionItemDetails){
        ArrayList<String> positions = new ArrayList<>();
        if( questionItemDetails.getFrozenTube() == null ||(questionItemDetails.getFrozenTube()!=null && questionItemDetails.getFrozenTube().getFrozenBox() ==null) ){
            return null;
        }
        FrozenBox frozenBox = questionItemDetails.getFrozenTube().getFrozenBox();
        if (frozenBox.getEquipmentCode() != null && frozenBox.getEquipmentCode().length() > 0){
            positions.add(frozenBox.getEquipmentCode());
        }

        if (frozenBox.getAreaCode() != null && frozenBox.getAreaCode().length() > 0) {
            positions.add(frozenBox.getAreaCode());
        }

        if (frozenBox.getSupportRackCode() != null && frozenBox.getSupportRackCode().length() > 0){
            positions.add(frozenBox.getSupportRackCode());
        }

        if (frozenBox.getRowsInShelf() != null && frozenBox.getRowsInShelf().length() > 0 && frozenBox.getColumnsInShelf() != null && frozenBox.getColumnsInShelf().length() > 0){
            positions.add(frozenBox.getColumnsInShelf()+frozenBox.getRowsInShelf());
        }
        return String.join(".",positions);

    }

    default QuestionItem questionItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestionItem questionItem = new QuestionItem();
        questionItem.setId(id);
        return questionItem;
    }

    default FrozenTube frozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTube frozenTube = new FrozenTube();
        frozenTube.setId(id);
        return frozenTube;
    }
}
