package org.fwoxford.repository;

import org.fwoxford.domain.Delegate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Delegate entity.
 */
@SuppressWarnings("unused")
public interface DelegateRepository extends JpaRepository<Delegate,Long> {

    List<Delegate> findByStatusNot(String status);

    Delegate findByDelegateCode(String delegateCode);

    Delegate findByDelegateName(String delegateName);

    Delegate findByIdAndStatus(Long delegateId, String valid);
}
