package org.fwoxford.repository;

import org.fwoxford.domain.QuartsTask;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the QuartsTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuartsTaskRepository extends JpaRepository<QuartsTask, Long>, JpaSpecificationExecutor<QuartsTask> {

}
