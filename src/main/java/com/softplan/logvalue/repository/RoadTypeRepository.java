package com.softplan.logvalue.repository;

import com.softplan.logvalue.domain.RoadType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoadType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoadTypeRepository extends JpaRepository<RoadType, Long> {

}
