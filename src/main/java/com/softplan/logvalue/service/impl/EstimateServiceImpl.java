package com.softplan.logvalue.service.impl;


import static com.softplan.logvalue.service.util.NullsUtil.*;

import com.softplan.logvalue.service.EstimateService;
import com.softplan.logvalue.service.UserService;
import com.softplan.logvalue.domain.Estimate;
import com.softplan.logvalue.domain.User;
import com.softplan.logvalue.domain.VehicleType;
import com.softplan.logvalue.repository.EstimateRepository;
import com.softplan.logvalue.repository.RoadTypeRepository;
import com.softplan.logvalue.repository.UserRepository;
import com.softplan.logvalue.repository.VehicleTypeRepository;
import com.softplan.logvalue.security.AuthoritiesConstants;
import com.softplan.logvalue.security.SecurityUtils;
import com.softplan.logvalue.security.UserNotActivatedException;
import com.softplan.logvalue.service.dto.EstimateDTO;
import com.softplan.logvalue.service.mapper.EstimateMapper;
import com.softplan.logvalue.web.rest.errors.BadRequestAlertException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
/**
 * Service Implementation for managing Estimate.
 */
@Service
@Transactional
public class EstimateServiceImpl implements EstimateService {

    private final Logger log = LoggerFactory.getLogger(EstimateServiceImpl.class);
    
    private static final String ENTITY_NAME = "estimate";

    private final EstimateRepository estimateRepository;

    private final EstimateMapper estimateMapper;


    private final UserRepository userRepository; 
    private final RoadTypeRepository roadTypeRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    

    public EstimateServiceImpl(EstimateRepository estimateRepository, 
                               EstimateMapper estimateMapper, 
                               UserRepository userRepository,
                               RoadTypeRepository roadTypeRepository,
                               VehicleTypeRepository vehicleTypeRepository
                               ) {
        this.estimateRepository = estimateRepository;
        this.estimateMapper = estimateMapper;
        this.roadTypeRepository = roadTypeRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a estimate.
     *
     * @param estimateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EstimateDTO save(EstimateDTO estimateDTO, User currenteUser) {
        log.debug("Request to save Estimate : {}", estimateDTO);

        // Validações
        validate(estimateDTO);

        // Executar calculo        
        calcFreightValue(estimateDTO);

        Estimate estimate = estimateMapper.toEntity(estimateDTO);

        // Usuario logado
        User onwer = this.userRepository.findById(currenteUser.getId()).get();
        estimate.setOwner(onwer);

        estimate.setCreatedAt(ZonedDateTime.now());

        estimate = estimateRepository.save(estimate);
        return estimateMapper.toDto(estimate);
    }

    private void validate(EstimateDTO estimateDTO) {

        if(estimateDTO == null) {
            throw new BadRequestAlertException("The estimate data is null", ENTITY_NAME, "");
        }

        if(estimateDTO.getVehicleTypeId() == null) {
        	throw new BadRequestAlertException("The Vehicle Type is null", ENTITY_NAME, "vehicletypeid");
        }
        
        if(estimateDTO.getPavedHighwayAmount() == null && estimateDTO.getNonPavedHighwayAmount() == null) {
        	throw new BadRequestAlertException("The Highway Amount is null", ENTITY_NAME, "distanceamount");
        } else {

            if(estimateDTO.getPavedHighwayAmount() != null && estimateDTO.getPavedHighwayAmount() < 0) {
                throw new BadRequestAlertException("The Highway Amount is invalid", ENTITY_NAME, "distanceamountInvalid");
            }

            if(estimateDTO.getNonPavedHighwayAmount() != null && estimateDTO.getNonPavedHighwayAmount() < 0) {
                throw new BadRequestAlertException("The Highway Amount is invalid", ENTITY_NAME, "distanceamountInvalid");
            }

        }
        
        if(estimateDTO.getLoadAmount() == null) {
        	throw new BadRequestAlertException("The Load Amount is null", ENTITY_NAME, "loadamount");
        }
    }


    private void calcFreightValue(EstimateDTO estimateDTO) {

    	Float currentValue = 0.0F;

        // Custo trecho pavimentado
        currentValue += pavedCost(estimateDTO);

        //Acredcimo por Rodovia Não Pavimentada
        currentValue += nonPavedCost(estimateDTO);

        //Acredcimo por tipo de Veículo 
        currentValue  = vehicleTypeCost(currentValue, estimateDTO);

        // Acrescimo por excesso de carga
        currentValue  = overloadCost(currentValue, estimateDTO);        

        // Pedagio
        currentValue  = tollCost(currentValue, estimateDTO);

        estimateDTO.setFreightAmount(currentValue.floatValue());
    }

    // Add Custo Pavimentado
    private Float pavedCost(EstimateDTO estimateDTO) {
        if(isNotNull(estimateDTO.getPavedHighwayAmount())) {
            Float factor = this.roadTypeRepository.findPaved().getFactor();
            return (estimateDTO.getPavedHighwayAmount() * factor);
        }
        return 0.0F;
    }

    // Add Custo Não Pavimentado
    private Float nonPavedCost(EstimateDTO estimateDTO) {
        if(isNotNull(estimateDTO.getNonPavedHighwayAmount())) {
            Float factor = this.roadTypeRepository.findNonPaved().getFactor();
            return (estimateDTO.getNonPavedHighwayAmount() * factor);
        }
        return 0.0F;
    }

    private Float vehicleTypeCost(Float currentValue, EstimateDTO estimateDTO) {

        if(isNotNull(estimateDTO.getVehicleTypeId())) {
            VehicleType vehicle = 
                this.vehicleTypeRepository.findById(estimateDTO.getVehicleTypeId())
                    .orElseThrow(() -> new BadRequestAlertException("The Vehicle Type is invalid", ENTITY_NAME, "vehicletypeidInvalid"));
            
            Float factor = vehicle.getFactor();

            return (currentValue * factor);
        }
        return currentValue;
    }

    private Float overloadCost(Float currentValue, EstimateDTO estimateDTO) {

        if(isNotNull(estimateDTO.getLoadAmount())) {

        	Float overloadFactor = 1.0F;

            VehicleType vehicle = 
                this.vehicleTypeRepository.findById(estimateDTO.getVehicleTypeId())
                    .orElseThrow(() -> new BadRequestAlertException("The Vehicle Type is invalid", ENTITY_NAME, "vehicletypeidInvalid"));

            if(estimateDTO.getLoadAmount() >= vehicle.getMaximumLoad()) {
                throw new BadRequestAlertException("Vehicle Overload", ENTITY_NAME, "vehicletypeoverload");
            } else

            if(estimateDTO.getLoadAmount() > vehicle.getRegularLoad()) {

                overloadFactor = 0.02F; // TODO Acrescentar esse informacao ao VehicleType.overloadFactor

                Integer overload = estimateDTO.getLoadAmount() - vehicle.getRegularLoad();

                Float overloadCost = (overload * overloadFactor);
                
                Integer distance = estimateDTO.getPavedHighwayAmount() + estimateDTO.getNonPavedHighwayAmount();

                return currentValue + ( distance * overloadCost);

            }

            // Sem sobrecarga
            return currentValue;
        }

        return 0.0F;
    }

    private Float tollCost(Float currentValue, EstimateDTO estimateDTO) {
        
        if(estimateDTO.isContainsToll() && isNotNull(estimateDTO.getTollValue())) {
            currentValue = currentValue + estimateDTO.getTollValue();
        }

        return currentValue;
    }

    /*@SuppressWarnings("static-access")
	public User getCurrentUser() {
    	
    	System.out.println("Current User >>>>>>>>> " + userService.getCurrentUser());
    	
        Optional<User> user = this.userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        return user.orElseThrow(() -> new UserNotActivatedException("User was not logged"));
    }*/

    /**
     * Get all the estimates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EstimateDTO> findAll(Pageable pageable, User currenteUser) {
        log.debug("Request to get all Estimates");

        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return estimateRepository.findAll(pageable)
                    .map(estimateMapper::toDto);
        } else {

            return estimateRepository.findAllByOwnerId(currenteUser.getId(), pageable)
                    .map(estimateMapper::toDto);
        }
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
