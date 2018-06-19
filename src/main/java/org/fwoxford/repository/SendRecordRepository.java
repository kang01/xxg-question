package org.fwoxford.repository;

import org.fwoxford.domain.SendRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SendRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SendRecordRepository extends JpaRepository<SendRecord, Long> {

}
