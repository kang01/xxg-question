package org.fwoxford.repository;

import org.fwoxford.domain.ReplyRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReplyRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReplyRecordRepository extends JpaRepository<ReplyRecord, Long> {

    ReplyRecord findBySendRecordId(Long sendRecordId);
}
