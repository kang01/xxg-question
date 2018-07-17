package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity FrozenTube and its DTO FrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenTubeMapper{

    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "sampleClassification.frontColor", target = "frontColorForClass")
    @Mapping(source = "sampleClassification.backColor", target = "backColorForClass")
    @Mapping(source = "sampleType.frontColor", target = "frontColor")
    @Mapping(source = "sampleType.backColor", target = "backColor")
    @Mapping(source = "sampleType.isMixed", target = "isMixed")
    FrozenTubeDTO frozenTubeToFrozenTubeDTO(FrozenTube frozenTube);

    List<FrozenTubeDTO> frozenTubesToFrozenTubeDTOs(List<FrozenTube> frozenTubes);

    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    FrozenTube frozenTubeDTOToFrozenTube(FrozenTubeDTO frozenTubeDTO);

    List<FrozenTube> frozenTubeDTOsToFrozenTubes(List<FrozenTubeDTO> frozenTubeDTOs);

    default FrozenTubeType frozenTubeTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTubeType frozenTubeType = new FrozenTubeType();
        frozenTubeType.setId(id);
        return frozenTubeType;
    }

    default SampleType sampleTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleType sampleType = new SampleType();
        sampleType.setId(id);
        return sampleType;
    }

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }

    default ProjectSite projectSiteFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectSite projectSite = new ProjectSite();
        projectSite.setId(id);
        return projectSite;
    }

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }
   default SampleClassification sampleClassificationFromId(Long id){
        if (id == null) {
            return null;
        }
        SampleClassification sampleClassification = new SampleClassification();
        sampleClassification.setId(id);
        return sampleClassification;
    }
    default List<FrozenTubeDTO> frozenTubesToFrozenTubeDTOsForSample(List<FrozenTube> frozenTubeList){
        if(frozenTubeList == null){
            return null;
        }
        List<FrozenTubeDTO> frozenTubeDTOS = new ArrayList<>();
        for(FrozenTube frozenTube :frozenTubeList){
            FrozenTubeDTO frozenTubeDTO = frozenTubeToFrozenTubeDTO(frozenTube);
            SampleClassification sampleClassification = frozenTube.getSampleClassification();
            if(sampleClassification != null){
                frozenTubeDTO.setSampleClassificationId(sampleClassification.getId());
                frozenTubeDTO.setSampleClassificationName(sampleClassification.getSampleClassificationName());
                frozenTubeDTO.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
                frozenTubeDTO.setFrontColorForClass(sampleClassification.getFrontColor());
                frozenTubeDTO.setBackColorForClass(sampleClassification.getBackColor());
            }
            SampleType sampleType = frozenTube.getSampleType();
            if(sampleType!=null){
                frozenTubeDTO.setSampleTypeId(sampleType.getId());
                frozenTubeDTO.setSampleTypeCode(sampleType.getSampleTypeCode());
                frozenTubeDTO.setSampleTypeName(sampleType.getSampleTypeName());
                frozenTubeDTO.setIsMixed(sampleType.getIsMixed());
                frozenTubeDTO.setFrontColor(sampleType.getFrontColor());
                frozenTubeDTO.setBackColor(sampleType.getBackColor());
            }
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        return frozenTubeDTOS;
    }

    default List<QuestionItemDetailsDTO> frozenTubesToQuestionSamples(List<FrozenTube> sampleList){
        if ( sampleList == null ) {
            return null;
        }

        List<QuestionItemDetailsDTO> list = new ArrayList<QuestionItemDetailsDTO>( sampleList.size() );
        for ( FrozenTube frozenTube : sampleList ) {
            list.add( frozenTubeDTOToQuestionSample( frozenTube ) );
        }

        return list;
    }

    default QuestionItemDetailsDTO frozenTubeDTOToQuestionSample(FrozenTube frozenTube){
        if(frozenTube == null){
            return null;
        }
        QuestionItemDetailsDTO resultDTO = new QuestionItemDetailsDTO();
        resultDTO.setId(frozenTube.getId());
        resultDTO.setSampleCode(frozenTube.getSampleCode());
        resultDTO.setSampleTypeName(frozenTube.getSampleTypeName());
        resultDTO.setSampleClassificationName(frozenTube.getSampleClassificationName());
        resultDTO.setProjectCode(frozenTube.getProjectCode());
        resultDTO.setFrozenBoxId(frozenTube.getFrozenBox().getId());
        resultDTO.setFrozenBoxCode(frozenTube.getFrozenBoxCode());
        resultDTO.setTubeRows(frozenTube.getTubeRows());
        resultDTO.setTubeColumns(frozenTube.getTubeColumns());
        resultDTO.setTag1(frozenTube.getTag1());
        resultDTO.setTag2(frozenTube.getTag2());
        resultDTO.setTag3(frozenTube.getTag3());
        resultDTO.setTag4(frozenTube.getTag4());
        resultDTO.setStatus(frozenTube.getStatus());
        resultDTO.setSampleTypeId(frozenTube.getSampleType()!=null?frozenTube.getSampleType().getId():null);
        resultDTO.setSampleTypeCode(frozenTube.getSampleTypeCode());
        resultDTO.setSampleClassificationId(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getId():null);
        resultDTO.setSampleClassificationCode(frozenTube.getSampleClassificationCode());
        resultDTO.setMemo(frozenTube.getMemo());
        return resultDTO;
    }
}
