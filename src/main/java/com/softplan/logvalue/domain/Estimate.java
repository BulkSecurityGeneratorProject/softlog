package com.softplan.logvalue.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * This class represent a calculation saved by the user
 */
@ApiModel(description = "This class represent a calculation saved by the user")
@Entity
@Table(name = "estimate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Estimate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "paved_highway_amount")
    private Integer pavedHighwayAmount;

    @Column(name = "non_paved_highway_amount")
    private Integer nonPavedHighwayAmount;

    @NotNull
    @Column(name = "load_amount")
    private Integer loadAmount;

    @NotNull
    @Column(name = "contains_toll")
    private Boolean containsToll;

    @Column(name = "toll_value")
    private Float tollValue;

    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties("")
    private RoadType roadType;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties("")
    private VehicleType vehicleType;


    @NotNull
    @ManyToOne
    @JsonIgnoreProperties("")
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPavedHighwayAmount() {
        return pavedHighwayAmount;
    }

    public Estimate pavedHighwayAmount(Integer pavedHighwayAmount) {
        this.pavedHighwayAmount = pavedHighwayAmount;
        return this;
    }

    public void setPavedHighwayAmount(Integer pavedHighwayAmount) {
        this.pavedHighwayAmount = pavedHighwayAmount;
    }

    public Integer getNonPavedHighwayAmount() {
        return nonPavedHighwayAmount;
    }

    public Estimate nonPavedHighwayAmount(Integer nonPavedHighwayAmount) {
        this.nonPavedHighwayAmount = nonPavedHighwayAmount;
        return this;
    }

    public void setNonPavedHighwayAmount(Integer nonPavedHighwayAmount) {
        this.nonPavedHighwayAmount = nonPavedHighwayAmount;
    }

    public Boolean isContainsToll() {
        return containsToll;
    }

    public Estimate containsToll(Boolean containsToll) {
        this.containsToll = containsToll;
        return this;
    }

    public void setContainsToll(Boolean containsToll) {
        this.containsToll = containsToll;
    }

    public Float getTollValue() {
        return tollValue;
    }

    public Estimate tollValue(Float tollValue) {
        this.tollValue = tollValue;
        return this;
    }

    public void setTollValue(Float tollValue) {
        this.tollValue = tollValue;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Estimate createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public RoadType getRoadType() {
        return roadType;
    }

    public Estimate roadType(RoadType roadType) {
        this.roadType = roadType;
        return this;
    }

    public void setRoadType(RoadType roadType) {
        this.roadType = roadType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Estimate vehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    /**
     * @return the loadAmount
     */
    public Integer getLoadAmount() {
        return loadAmount;
    }

    /**
     * @param loadAmount the loadAmount to set
     */
    public void setLoadAmount(Integer loadAmount) {
        this.loadAmount = loadAmount;
    }

    /**
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(User owner) {
        this.owner = owner;
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
        Estimate estimate = (Estimate) o;
        if (estimate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estimate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Estimate{" +
            "id=" + getId() +
            ", pavedHighwayAmount=" + getPavedHighwayAmount() +
            ", nonPavedHighwayAmount=" + getNonPavedHighwayAmount() +
            ", containsToll='" + isContainsToll() + "'" +
            ", tollValue=" + getTollValue() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
