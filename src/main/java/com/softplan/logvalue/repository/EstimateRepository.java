package com.softplan.logvalue.repository;

import com.softplan.logvalue.domain.Estimate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Estimate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    //@Query("select o from Estimate o where o.owner.id = ?#{principal.emailAddress}")
    Page<Estimate> findAllByOwnerId(Long id, Pageable pageable);
   
}
