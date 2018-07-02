package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.QuartzTask;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the QuartzTask entity.
 */
@SuppressWarnings("unused")
public interface QuartzTaskRepository extends JpaRepository<QuartzTask, Long> {
    @Query("select t from QuartzTask t where t.enableStatus = ?1 and t.status = '"+Constants.VALID+"' ")
    List<QuartzTask> findByEnableStatus(Integer taskEnableStatusYes);
}
