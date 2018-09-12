package com.softplan.logvalue.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.softplan.logvalue.service.RoadTypeService;
import com.softplan.logvalue.web.rest.errors.BadRequestAlertException;
import com.softplan.logvalue.web.rest.util.HeaderUtil;
import com.softplan.logvalue.web.rest.util.PaginationUtil;
import com.softplan.logvalue.service.dto.RoadTypeDTO;
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
 * REST controller for managing RoadType.
 */
@RestController
@RequestMapping("/api")
public class RoadTypeResource {

    private final Logger log = LoggerFactory.getLogger(RoadTypeResource.class);

    private static final String ENTITY_NAME = "roadType";

    private final RoadTypeService roadTypeService;

    public RoadTypeResource(RoadTypeService roadTypeService) {
        this.roadTypeService = roadTypeService;
    }

    /**
     * POST  /road-types : Create a new roadType.
     *
     * @param roadTypeDTO the roadTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roadTypeDTO, or with status 400 (Bad Request) if the roadType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/road-types")
    @Timed
    public ResponseEntity<RoadTypeDTO> createRoadType(@RequestBody RoadTypeDTO roadTypeDTO) throws URISyntaxException {
        log.debug("REST request to save RoadType : {}", roadTypeDTO);
        if (roadTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new roadType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoadTypeDTO result = roadTypeService.save(roadTypeDTO);
        return ResponseEntity.created(new URI("/api/road-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /road-types : Updates an existing roadType.
     *
     * @param roadTypeDTO the roadTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roadTypeDTO,
     * or with status 400 (Bad Request) if the roadTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the roadTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/road-types")
    @Timed
    public ResponseEntity<RoadTypeDTO> updateRoadType(@RequestBody RoadTypeDTO roadTypeDTO) throws URISyntaxException {
        log.debug("REST request to update RoadType : {}", roadTypeDTO);
        if (roadTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoadTypeDTO result = roadTypeService.save(roadTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roadTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /road-types : get all the roadTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roadTypes in body
     */
    @GetMapping("/road-types")
    @Timed
    public ResponseEntity<List<RoadTypeDTO>> getAllRoadTypes(Pageable pageable) {
        log.debug("REST request to get a page of RoadTypes");
        Page<RoadTypeDTO> page = roadTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/road-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /road-types/:id : get the "id" roadType.
     *
     * @param id the id of the roadTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roadTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/road-types/{id}")
    @Timed
    public ResponseEntity<RoadTypeDTO> getRoadType(@PathVariable Long id) {
        log.debug("REST request to get RoadType : {}", id);
        Optional<RoadTypeDTO> roadTypeDTO = roadTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roadTypeDTO);
    }

    /**
     * DELETE  /road-types/:id : delete the "id" roadType.
     *
     * @param id the id of the roadTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/road-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoadType(@PathVariable Long id) {
        log.debug("REST request to delete RoadType : {}", id);
        roadTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
