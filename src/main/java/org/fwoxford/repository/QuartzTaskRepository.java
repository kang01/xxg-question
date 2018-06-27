package org.fwoxford.repository;

import org.fwoxford.domain.QuartzTask;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the QuartzTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuartzTaskRepository extends JpaRepository<QuartzTask, Long>, JpaSpecificationExecutor<QuartzTask> {

    List<QuartzTask> findByEnableStatus(Integer taskEnableStatusYes);
}
