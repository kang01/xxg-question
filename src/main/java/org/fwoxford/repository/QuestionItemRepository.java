package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.QuestionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the QuestionItem entity.
 */
@SuppressWarnings("unused")
public interface QuestionItemRepository extends JpaRepository<QuestionItem,Long> {

    List<QuestionItem> findByQuestionIdAndStatusNot(Long id, String status);

    QuestionItem findByIdAndStatusNot(Long id, String invalid);
    @Query("SELECT t.id FROM QuestionItem t WHERE t.question.id = ?1 and t.status !='"+ Constants.INVALID+"'")
    List<Long> findIdByQuestionId(Long questionId);
}
