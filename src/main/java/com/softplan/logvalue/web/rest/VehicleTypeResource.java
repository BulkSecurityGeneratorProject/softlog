package com.softplan.logvalue.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.softplan.logvalue.service.VehicleTypeService;
import com.softplan.logvalue.web.rest.errors.BadRequestAlertException;
import com.softplan.logvalue.web.rest.util.HeaderUtil;
import com.softplan.logvalue.web.rest.util.PaginationUtil;
import com.softplan.logvalue.service.dto.VehicleTypeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VehicleType.
 */
@RestController
@RequestMapping("/api")
public class VehicleTypeResource {

    private final Logger log = LoggerFactory.getLogger(VehicleTypeResource.class);

    private static final String ENTITY_NAME = "vehicleType";

    private final VehicleTypeService vehicleTypeService;

    public VehicleTypeResource(VehicleTypeService vehicleTypeService) {
        this.vehicleTypeService = vehicleTypeService;
    }

    /**
     * POST  /vehicle-types : Create a new vehicleType.
     *
     * @param vehicleTypeDTO the vehicleTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vehicleTypeDTO, or with status 400 (Bad Request) if the vehicleType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vehicle-types")
    @Timed
    public ResponseEntity<VehicleTypeDTO> createVehicleType(@RequestBody VehicleTypeDTO vehicleTypeDTO) throws URISyntaxException {
        log.debug("REST request to save VehicleType : {}", vehicleTypeDTO);
        if (vehicleTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehicleTypeDTO result = vehicleTypeService.save(vehicleTypeDTO);
        return ResponseEntity.created(new URI("/api/vehicle-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vehicle-types : Updates an existing vehicleType.
     *
     * @param vehicleTypeDTO the vehicleTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vehicleTypeDTO,
     * or with status 400 (Bad Request) if the vehicleTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the vehicleTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vehicle-types")
    @Timed
    public ResponseEntity<VehicleTypeDTO> updateVehicleType(@RequestBody VehicleTypeDTO vehicleTypeDTO) throws URISyntaxException {
        log.debug("REST request to update VehicleType : {}", vehicleTypeDTO);
        if (vehicleTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VehicleTypeDTO result = vehicleTypeService.save(vehicleTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vehicleTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vehicle-types : get all the vehicleTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of vehicleTypes in body
     */
    @GetMapping("/vehicle-types")
    @Timed
    public ResponseEntity<List<VehicleTypeDTO>> getAllVehicleTypes(Pageable pageable) {
        log.debug("REST request to get a page of VehicleTypes");
        Page<VehicleTypeDTO> page = vehicleTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vehicle-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vehicle-types/:id : get the "id" vehicleType.
     *
     * @param id the id of the vehicleTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vehicleTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/vehicle-types/{id}")
    @Timed
    public ResponseEntity<VehicleTypeDTO> getVehicleType(@PathVariable Long id) {
        log.debug("REST request to get VehicleType : {}", id);
        Optional<VehicleTypeDTO> vehicleTypeDTO = vehicleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleTypeDTO);
    }

    /**
     * DELETE  /vehicle-types/:id : delete the "id" vehicleType.
     *
     * @param id the id of the vehicleTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vehicle-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteVehicleType(@PathVariable Long id) {
        log.debug("REST request to delete VehicleType : {}", id);
        vehicleTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
