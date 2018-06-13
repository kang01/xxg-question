package org.fwoxford.service;
import org.fwoxford.core.service.dto.response.DelegateResponse;
import org.fwoxford.core.service.dto.response.FrozenTubeListAllDataTableEntity;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing Delegate.
 */
public interface FrozenTubeService {

    List<QuestionItemDetailsDTO> findQuestionFrozenTube(QuestionItemDetailsDTO questionItemDetailsDTO);
}
