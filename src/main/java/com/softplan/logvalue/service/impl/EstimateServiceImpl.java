package com.softplan.logvalue.service.impl;

import com.softplan.logvalue.service.EstimateService;
import com.softplan.logvalue.domain.Estimate;
import com.softplan.logvalue.repository.EstimateRepository;
import com.softplan.logvalue.service.dto.EstimateDTO;
import com.softplan.logvalue.service.mapper.EstimateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Estimate.
 */
@Service
@Transactional
public class EstimateServiceImpl implements EstimateService {

    private final Logger log = LoggerFactory.getLogger(EstimateServiceImpl.class);

    private final EstimateRepository estimateRepository;

    private final EstimateMapper estimateMapper;

    public EstimateServiceImpl(EstimateRepository estimateRepository, EstimateMapper estimateMapper) {
        this.estimateRepository = estimateRepository;
        this.estimateMapper = estimateMapper;
    }

    /**
     * Save a estimate.
     *
     * @param estimateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EstimateDTO save(EstimateDTO estimateDTO) {
        log.debug("Request to save Estimate : {}", estimateDTO);
        Estimate estimate = estimateMapper.toEntity(estimateDTO);
        estimate = estimateRepository.save(estimate);
        return estimateMapper.toDto(estimate);
    }

    /**
     * Get all the estimates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EstimateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estimates");
        return estimateRepository.findAll(pageable)
            .map(estimateMapper::toDto);
    }


    /**
     * Get one estimate by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EstimateDTO> findOne(Long id) {
        log.debug("Request to get Estimate : {}", id);
        return estimateRepository.findById(id)
            .map(estimateMapper::toDto);
    }

    /**
     * Delete the estimate by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Estimate : {}", id);
        estimateRepository.deleteById(id);
    }
}
