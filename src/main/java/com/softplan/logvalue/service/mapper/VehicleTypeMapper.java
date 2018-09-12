package com.softplan.logvalue.service.mapper;

import com.softplan.logvalue.domain.*;
import com.softplan.logvalue.service.dto.VehicleTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity VehicleType and its DTO VehicleTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VehicleTypeMapper extends EntityMapper<VehicleTypeDTO, VehicleType> {



    default VehicleType fromId(Long id) {
        if (id == null) {
            return null;
        }
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(id);
        return vehicleType;
    }
}
