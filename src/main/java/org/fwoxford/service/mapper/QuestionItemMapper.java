package org.fwoxford.service.mapper;

import org.fwoxford.domain.Question;
import org.fwoxford.domain.QuestionItem;
import org.fwoxford.service.dto.QuestionItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity QuestionItem and its DTO QuestionItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionItemMapper {

    @Mapping(source = "question.id", target = "questionId")
    QuestionItemDTO questionItemToQuestionItemDTO(QuestionItem questionItem);

    List<QuestionItemDTO> questionItemsToQuestionItemDTOs(List<QuestionItem> questionItems);

    @Mapping(source = "questionId", target = "question")
    QuestionItem questionItemDTOToQuestionItem(QuestionItemDTO questionItemDTO);

    List<QuestionItem> questionItemDTOsToQuestionItems(List<QuestionItemDTO> questionItemDTOs);

    default Question questionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }
}
