package org.fwoxford.repository;

import org.fwoxford.domain.QuestionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the QuestionItem entity.
 */
@SuppressWarnings("unused")
public interface QuestionItemRepository extends JpaRepository<QuestionItem,Long> {

    List<QuestionItem> findByQuestionIdAndStatusNot(Long id, String status);

    QuestionItem findByIdAndStatusNot(Long id, String invalid);
}
