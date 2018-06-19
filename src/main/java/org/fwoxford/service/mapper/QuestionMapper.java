package org.fwoxford.service.mapper;

import org.fwoxford.domain.Project;
import org.fwoxford.domain.Question;
import org.fwoxford.service.dto.QuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Question and its DTO QuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionMapper {

    @Mapping(source = "project.id", target = "projectId")
    QuestionDTO questionToQuestionDTO(Question question);

    List<QuestionDTO> questionsToQuestionDTOs(List<Question> questions);

    @Mapping(source = "projectId", target = "project")
    Question questionDTOToQuestion(QuestionDTO questionDTO);

    List<Question> questionDTOsToQuestions(List<QuestionDTO> questionDTOs);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
