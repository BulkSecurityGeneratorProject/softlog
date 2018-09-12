package com.softplan.logvalue.service.impl;

import com.softplan.logvalue.service.VehicleTypeService;
import com.softplan.logvalue.domain.VehicleType;
import com.softplan.logvalue.repository.VehicleTypeRepository;
import com.softplan.logvalue.service.dto.VehicleTypeDTO;
import com.softplan.logvalue.service.mapper.VehicleTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing VehicleType.
 */
@Service
@Transactional
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final Logger log = LoggerFactory.getLogger(VehicleTypeServiceImpl.class);

    private final VehicleTypeRepository vehicleTypeRepository;

    private final VehicleTypeMapper vehicleTypeMapper;

    public VehicleTypeServiceImpl(VehicleTypeRepository vehicleTypeRepository, VehicleTypeMapper vehicleTypeMapper) {
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.vehicleTypeMapper = vehicleTypeMapper;
    }

    /**
     * Save a vehicleType.
     *
     * @param vehicleTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VehicleTypeDTO save(VehicleTypeDTO vehicleTypeDTO) {
        log.debug("Request to save VehicleType : {}", vehicleTypeDTO);
        VehicleType vehicleType = vehicleTypeMapper.toEntity(vehicleTypeDTO);
        vehicleType = vehicleTypeRepository.save(vehicleType);
        return vehicleTypeMapper.toDto(vehicleType);
    }

    /**
     * Get all the vehicleTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VehicleTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleTypes");
        return vehicleTypeRepository.findAll(pageable)
            .map(vehicleTypeMapper::toDto);
    }


    /**
     * Get one vehicleType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleTypeDTO> findOne(Long id) {
        log.debug("Request to get VehicleType : {}", id);
        return vehicleTypeRepository.findById(id)
            .map(vehicleTypeMapper::toDto);
    }

    /**
     * Delete the vehicleType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleType : {}", id);
        vehicleTypeRepository.deleteById(id);
    }
}
