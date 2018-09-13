package com.softplan.logvalue.repository;

import com.softplan.logvalue.domain.RoadType;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoadType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoadTypeRepository extends JpaRepository<RoadType, Long> {

    String ROAD_TYPE_CACHE = "roadTypeCache";

    @Query("SELECT r FROM RoadType r WHERE r.id = 1")
    // @Cacheable(cacheNames = ROAD_TYPE_CACHE+"2")
    RoadType findPaved();

    @Query("SELECT r FROM RoadType r WHERE r.id = 2")
    // @Cacheable(cacheNames = ROAD_TYPE_CACHE+"2")
    RoadType findNonPaved();
    
}
