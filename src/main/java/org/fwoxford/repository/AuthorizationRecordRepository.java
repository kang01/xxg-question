package org.fwoxford.repository;

import org.fwoxford.domain.AuthorizationRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the AuthorizationRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorizationRecordRepository extends JpaRepository<AuthorizationRecord, Long> {

    List<AuthorizationRecord> findByQuestionIdAndStatusNot(Long questionId, String invalid);
}
