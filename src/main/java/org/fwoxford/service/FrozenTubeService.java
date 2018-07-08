package org.fwoxford.service;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;

import java.util.List;

/**
 * Service Interface for managing Delegate.
 */
public interface FrozenTubeService {

    List<QuestionItemDetailsDTO> findQuestionFrozenTube(QuestionItemDetailsDTO questionItemDetailsDTO);

    Long countQuestionFrozenTube(QuestionItemDetailsDTO questionItemDetailsDTO);
}
