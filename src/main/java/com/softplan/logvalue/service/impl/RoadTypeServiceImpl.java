package com.softplan.logvalue.service.impl;

import com.softplan.logvalue.service.RoadTypeService;
import com.softplan.logvalue.domain.RoadType;
import com.softplan.logvalue.repository.RoadTypeRepository;
import com.softplan.logvalue.service.dto.RoadTypeDTO;
import com.softplan.logvalue.service.mapper.RoadTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing RoadType.
 */
@Service
@Transactional
public class RoadTypeServiceImpl implements RoadTypeService {

    private final Logger log = LoggerFactory.getLogger(RoadTypeServiceImpl.class);

    private final RoadTypeRepository roadTypeRepository;

    private final RoadTypeMapper roadTypeMapper;

    public RoadTypeServiceImpl(RoadTypeRepository roadTypeRepository, RoadTypeMapper roadTypeMapper) {
        this.roadTypeRepository = roadTypeRepository;
        this.roadTypeMapper = roadTypeMapper;
    }

    /**
     * Save a roadType.
     *
     * @param roadTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RoadTypeDTO save(RoadTypeDTO roadTypeDTO) {
        log.debug("Request to save RoadType : {}", roadTypeDTO);
        RoadType roadType = roadTypeMapper.toEntity(roadTypeDTO);
        roadType = roadTypeRepository.save(roadType);
        return roadTypeMapper.toDto(roadType);
    }

    /**
     * Get all the roadTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RoadTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoadTypes");
        return roadTypeRepository.findAll(pageable)
            .map(roadTypeMapper::toDto);
    }


    /**
     * Get one roadType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RoadTypeDTO> findOne(Long id) {
        log.debug("Request to get RoadType : {}", id);
        return roadTypeRepository.findById(id)
            .map(roadTypeMapper::toDto);
    }

    /**
     * Delete the roadType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoadType : {}", id);
        roadTypeRepository.deleteById(id);
    }
}
