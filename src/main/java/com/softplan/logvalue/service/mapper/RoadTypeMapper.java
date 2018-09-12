package com.softplan.logvalue.service.mapper;

import com.softplan.logvalue.domain.*;
import com.softplan.logvalue.service.dto.RoadTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RoadType and its DTO RoadTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoadTypeMapper extends EntityMapper<RoadTypeDTO, RoadType> {



    default RoadType fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoadType roadType = new RoadType();
        roadType.setId(id);
        return roadType;
    }
}
