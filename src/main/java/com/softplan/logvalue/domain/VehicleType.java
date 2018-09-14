package com.softplan.logvalue.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represent a type of Vehicle
 */
@ApiModel(description = "This class represent a type of Vehicle")
@Entity
@Table(name = "vehicle_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VehicleType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "factor")
    private Float factor;

    @Column(name = "regular_load")
    private Integer regularLoad;

    @Column(name = "maximum_load")
    private Integer maximumLoad;

    public VehicleType() {
        super();
    }

    public VehicleType(Long id) {
        super();
        this.id = id;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public VehicleType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getFactor() {
        return factor;
    }

    public VehicleType factor(Float factor) {
        this.factor = factor;
        return this;
    }

    public void setFactor(Float factor) {
        this.factor = factor;
    }

    public Integer getRegularLoad() {
        return regularLoad;
    }

    public VehicleType regularLoad(Integer regularLoad) {
        this.regularLoad = regularLoad;
        return this;
    }

    public void setRegularLoad(Integer regularLoad) {
        this.regularLoad = regularLoad;
    }

    public Integer getMaximumLoad() {
        return maximumLoad;
    }

    public VehicleType maximumLoad(Integer maximumLoad) {
        this.maximumLoad = maximumLoad;
        return this;
    }

    public void setMaximumLoad(Integer maximumLoad) {
        this.maximumLoad = maximumLoad;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VehicleType vehicleType = (VehicleType) o;
        if (vehicleType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicleType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehicleType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", factor=" + getFactor() +
            ", regularLoad=" + getRegularLoad() +
            ", maximumLoad=" + getMaximumLoad() +
            "}";
    }
}
