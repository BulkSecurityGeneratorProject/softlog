package com.softplan.logvalue.service;

import com.softplan.logvalue.domain.User;
import com.softplan.logvalue.service.dto.EstimateDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Estimate.
 */
public interface EstimateService {

    /**
     * Save a estimate.
     *
     * @param estimateDTO the entity to save
     * @return the persisted entity
     */
    EstimateDTO save(EstimateDTO estimateDTO, User currenteUser);

    /**
     * Get all the estimates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EstimateDTO> findAll(Pageable pageable);


    /**
     * Get the "id" estimate.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EstimateDTO> findOne(Long id);

    /**
     * Delete the "id" estimate.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
           
}
