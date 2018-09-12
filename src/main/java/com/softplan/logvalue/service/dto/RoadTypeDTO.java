package com.softplan.logvalue.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RoadType entity.
 */
public class RoadTypeDTO implements Serializable {

    private Long id;

    private String name;

    private Float factor;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoadTypeDTO roadTypeDTO = (RoadTypeDTO) o;
        if (roadTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roadTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoadTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", factor=" + getFactor() +
            "}";
    }
}
