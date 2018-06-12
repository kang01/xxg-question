package org.fwoxford.repository;

import org.fwoxford.service.dto.response.QuestionForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
public interface QuestionRepositories extends DataTablesRepository<QuestionForDataTableEntity,Long> {

}
