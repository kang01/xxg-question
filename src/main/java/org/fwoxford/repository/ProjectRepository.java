package org.fwoxford.repository;

import org.fwoxford.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query("select t from Project t  where t.status != '0000'")
    List<Project> findAllProject();

    @Query("select t from Project t  where t.projectCode = ?1 and t.status != '0000'")
    Project findByProjectCode(String projectCode);

    Project findByIdAndStatus(Long projectId, String valid);
}
