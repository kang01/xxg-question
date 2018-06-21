package org.fwoxford.repository;

import org.fwoxford.domain.ReplyDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReplyDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReplyDetailsRepository extends JpaRepository<ReplyDetails, Long> {

}
