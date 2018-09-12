package com.softplan.logvalue.service;

import com.softplan.logvalue.service.dto.VehicleTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing VehicleType.
 */
public interface VehicleTypeService {

    /**
     * Save a vehicleType.
     *
     * @param vehicleTypeDTO the entity to save
     * @return the persisted entity
     */
    VehicleTypeDTO save(VehicleTypeDTO vehicleTypeDTO);

    /**
     * Get all the vehicleTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<VehicleTypeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" vehicleType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<VehicleTypeDTO> findOne(Long id);

    /**
     * Delete the "id" vehicleType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
