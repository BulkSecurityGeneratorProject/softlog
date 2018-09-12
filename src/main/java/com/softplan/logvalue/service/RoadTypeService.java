package com.softplan.logvalue.service;

import com.softplan.logvalue.service.dto.RoadTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing RoadType.
 */
public interface RoadTypeService {

    /**
     * Save a roadType.
     *
     * @param roadTypeDTO the entity to save
     * @return the persisted entity
     */
    RoadTypeDTO save(RoadTypeDTO roadTypeDTO);

    /**
     * Get all the roadTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RoadTypeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" roadType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RoadTypeDTO> findOne(Long id);

    /**
     * Delete the "id" roadType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
