package com.softplan.logvalue.repository;

import com.softplan.logvalue.domain.Estimate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Estimate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {

}
