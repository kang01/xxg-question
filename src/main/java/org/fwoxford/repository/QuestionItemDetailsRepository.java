package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.QuestionItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the QuestionItemDetails entity.
 */
@SuppressWarnings("unused")
public interface QuestionItemDetailsRepository extends JpaRepository<QuestionItemDetails,Long> {

    @Modifying
    @Query("update QuestionItemDetails t set t.status = '"+ Constants.INVALID+" ' where t.questionItem.id = ?1")
    void updateStatusByQuestionItemquestionItemId(Long id);

    List<QuestionItemDetails> findByQuestionItemIdAndStatusNot(Long id, String status);

    @Query("select count(t.id) from  QuestionItemDetails t where t.status != '"+ Constants.INVALID+" ' and t.questionItem.question.id = ?1")
    Long countByQuestionId(Long questionId);
}
