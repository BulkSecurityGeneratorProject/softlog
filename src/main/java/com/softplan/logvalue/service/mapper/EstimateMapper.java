package com.softplan.logvalue.service.mapper;

import com.softplan.logvalue.domain.*;
import com.softplan.logvalue.service.dto.EstimateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Estimate and its DTO EstimateDTO.
 */
@Mapper(componentModel = "spring", uses = {RoadTypeMapper.class, VehicleTypeMapper.class})
public interface EstimateMapper extends EntityMapper<EstimateDTO, Estimate> {

    @Mapping(source = "vehicleType.id", target = "vehicleTypeId")
    @Mapping(source = "vehicleType.name", target = "vehicleTypeName")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.firstName", target = "ownerName")
    EstimateDTO toDto(Estimate estimate);

    @Mapping(source = "vehicleTypeId", target = "vehicleType")
    Estimate toEntity(EstimateDTO estimateDTO);

    default Estimate fromId(Long id) {
        if (id == null) {
            return null;
        }
        Estimate estimate = new Estimate();
        estimate.setId(id);
        return estimate;
    }
}
