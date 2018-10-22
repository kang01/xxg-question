package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.SendRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Spring Data JPA repository for the SendRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SendRecordRepository extends JpaRepository<SendRecord, Long> {
    @Query("SELECT count(t.id) FROM SendRecord t WHERE t.questionId = ?1 and t.status != '"+ Constants.QUESTION_SEND_REPLIED+"'")
    Long countUnReplyRecordByQuestionId(Long questionId);

    List<SendRecord> findByQuestionIdAndStatusNot(Long id, String status);

    List<SendRecord> findByQuestionId(Long questionId);

    Long countByQuestionIdAndStatus(Long questionId, String status);

    Long countByQuestionIdAndStatusNot(Long id, String status);

    SendRecord findByAuthorizationRecordIdAndStatusNotIn(Long id,  ArrayList<String> statusList);
}
