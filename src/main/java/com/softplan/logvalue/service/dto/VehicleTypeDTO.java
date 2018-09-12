package com.softplan.logvalue.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the VehicleType entity.
 */
public class VehicleTypeDTO implements Serializable {

    private Long id;

    private String name;

    private Float factor;

    private Integer regularLoad;

    private Integer maximumLoad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getFactor() {
        return factor;
    }

    public void setFactor(Float factor) {
        this.factor = factor;
    }

    public Integer getRegularLoad() {
        return regularLoad;
    }

    public void setRegularLoad(Integer regularLoad) {
        this.regularLoad = regularLoad;
    }

    public Integer getMaximumLoad() {
        return maximumLoad;
    }

    public void setMaximumLoad(Integer maximumLoad) {
        this.maximumLoad = maximumLoad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VehicleTypeDTO vehicleTypeDTO = (VehicleTypeDTO) o;
        if (vehicleTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicleTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehicleTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", factor=" + getFactor() +
            ", regularLoad=" + getRegularLoad() +
            ", maximumLoad=" + getMaximumLoad() +
            "}";
    }
}
