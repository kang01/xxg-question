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
    @Query("update QuestionItemDetails t set t.status = '"+ Constants.INVALID+"' where t.questionItem.id = ?1")
    void updateStatusByQuestionItemquestionItemId(Long id);

    List<QuestionItemDetails> findByQuestionItemIdAndStatusNot(Long id, String status);

    @Query("select count(t.id) from  QuestionItemDetails t where t.status != '"+ Constants.INVALID+"' and t.questionItem.question.id = ?1")
    Long countByQuestionId(Long questionId);

    @Query(value = "select frozen_tube_id,frozen_box_code,frozen_box_code_1d from (" +
        "      select row_number() over(partition by frozen_tube_id order by CREATED_DATE asc) rn, a.* from  tranship_tube a where frozen_tube_id in ?1 and a.status !='0000' " +
        "       ) where rn = 1",nativeQuery = true)
    List<Object[]> findByFrozenTubeIdsIn(List<Long> ids);
}
