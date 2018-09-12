package com.softplan.logvalue.web.rest;

import com.softplan.logvalue.LogValueApp;

import com.softplan.logvalue.domain.VehicleType;
import com.softplan.logvalue.repository.VehicleTypeRepository;
import com.softplan.logvalue.service.VehicleTypeService;
import com.softplan.logvalue.service.dto.VehicleTypeDTO;
import com.softplan.logvalue.service.mapper.VehicleTypeMapper;
import com.softplan.logvalue.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.softplan.logvalue.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VehicleTypeResource REST controller.
 *
 * @see VehicleTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogValueApp.class)
public class VehicleTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_FACTOR = 1F;
    private static final Float UPDATED_FACTOR = 2F;

    private static final Integer DEFAULT_REGULAR_LOAD = 1;
    private static final Integer UPDATED_REGULAR_LOAD = 2;

    private static final Integer DEFAULT_MAXIMUM_LOAD = 1;
    private static final Integer UPDATED_MAXIMUM_LOAD = 2;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;


    @Autowired
    private VehicleTypeMapper vehicleTypeMapper;
    

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVehicleTypeMockMvc;

    private VehicleType vehicleType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VehicleTypeResource vehicleTypeResource = new VehicleTypeResource(vehicleTypeService);
        this.restVehicleTypeMockMvc = MockMvcBuilders.standaloneSetup(vehicleTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleType createEntity(EntityManager em) {
        VehicleType vehicleType = new VehicleType()
            .name(DEFAULT_NAME)
            .factor(DEFAULT_FACTOR)
            .regularLoad(DEFAULT_REGULAR_LOAD)
            .maximumLoad(DEFAULT_MAXIMUM_LOAD);
        return vehicleType;
    }

    @Before
    public void initTest() {
        vehicleType = createEntity(em);
    }

    @Test
    @Transactional
    public void createVehicleType() throws Exception {
        int databaseSizeBeforeCreate = vehicleTypeRepository.findAll().size();

        // Create the VehicleType
        VehicleTypeDTO vehicleTypeDTO = vehicleTypeMapper.toDto(vehicleType);
        restVehicleTypeMockMvc.perform(post("/api/vehicle-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the VehicleType in the database
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll();
        assertThat(vehicleTypeList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleType testVehicleType = vehicleTypeList.get(vehicleTypeList.size() - 1);
        assertThat(testVehicleType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVehicleType.getFactor()).isEqualTo(DEFAULT_FACTOR);
        assertThat(testVehicleType.getRegularLoad()).isEqualTo(DEFAULT_REGULAR_LOAD);
        assertThat(testVehicleType.getMaximumLoad()).isEqualTo(DEFAULT_MAXIMUM_LOAD);
    }

    @Test
    @Transactional
    public void createVehicleTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehicleTypeRepository.findAll().size();

        // Create the VehicleType with an existing ID
        vehicleType.setId(1L);
        VehicleTypeDTO vehicleTypeDTO = vehicleTypeMapper.toDto(vehicleType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleTypeMockMvc.perform(post("/api/vehicle-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleType in the database
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll();
        assertThat(vehicleTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVehicleTypes() throws Exception {
        // Initialize the database
        vehicleTypeRepository.saveAndFlush(vehicleType);

        // Get all the vehicleTypeList
        restVehicleTypeMockMvc.perform(get("/api/vehicle-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].factor").value(hasItem(DEFAULT_FACTOR.doubleValue())))
            .andExpect(jsonPath("$.[*].regularLoad").value(hasItem(DEFAULT_REGULAR_LOAD)))
            .andExpect(jsonPath("$.[*].maximumLoad").value(hasItem(DEFAULT_MAXIMUM_LOAD)));
    }
    

    @Test
    @Transactional
    public void getVehicleType() throws Exception {
        // Initialize the database
        vehicleTypeRepository.saveAndFlush(vehicleType);

        // Get the vehicleType
        restVehicleTypeMockMvc.perform(get("/api/vehicle-types/{id}", vehicleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.factor").value(DEFAULT_FACTOR.doubleValue()))
            .andExpect(jsonPath("$.regularLoad").value(DEFAULT_REGULAR_LOAD))
            .andExpect(jsonPath("$.maximumLoad").value(DEFAULT_MAXIMUM_LOAD));
    }
    @Test
    @Transactional
    public void getNonExistingVehicleType() throws Exception {
        // Get the vehicleType
        restVehicleTypeMockMvc.perform(get("/api/vehicle-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicleType() throws Exception {
        // Initialize the database
        vehicleTypeRepository.saveAndFlush(vehicleType);

        int databaseSizeBeforeUpdate = vehicleTypeRepository.findAll().size();

        // Update the vehicleType
        VehicleType updatedVehicleType = vehicleTypeRepository.findById(vehicleType.getId()).get();
        // Disconnect from session so that the updates on updatedVehicleType are not directly saved in db
        em.detach(updatedVehicleType);
        updatedVehicleType
            .name(UPDATED_NAME)
            .factor(UPDATED_FACTOR)
            .regularLoad(UPDATED_REGULAR_LOAD)
            .maximumLoad(UPDATED_MAXIMUM_LOAD);
        VehicleTypeDTO vehicleTypeDTO = vehicleTypeMapper.toDto(updatedVehicleType);

        restVehicleTypeMockMvc.perform(put("/api/vehicle-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleTypeDTO)))
            .andExpect(status().isOk());

        // Validate the VehicleType in the database
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll();
        assertThat(vehicleTypeList).hasSize(databaseSizeBeforeUpdate);
        VehicleType testVehicleType = vehicleTypeList.get(vehicleTypeList.size() - 1);
        assertThat(testVehicleType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVehicleType.getFactor()).isEqualTo(UPDATED_FACTOR);
        assertThat(testVehicleType.getRegularLoad()).isEqualTo(UPDATED_REGULAR_LOAD);
        assertThat(testVehicleType.getMaximumLoad()).isEqualTo(UPDATED_MAXIMUM_LOAD);
    }

    @Test
    @Transactional
    public void updateNonExistingVehicleType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleTypeRepository.findAll().size();

        // Create the VehicleType
        VehicleTypeDTO vehicleTypeDTO = vehicleTypeMapper.toDto(vehicleType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVehicleTypeMockMvc.perform(put("/api/vehicle-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehicleTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleType in the database
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll();
        assertThat(vehicleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVehicleType() throws Exception {
        // Initialize the database
        vehicleTypeRepository.saveAndFlush(vehicleType);

        int databaseSizeBeforeDelete = vehicleTypeRepository.findAll().size();

        // Get the vehicleType
        restVehicleTypeMockMvc.perform(delete("/api/vehicle-types/{id}", vehicleType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll();
        assertThat(vehicleTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleType.class);
        VehicleType vehicleType1 = new VehicleType();
        vehicleType1.setId(1L);
        VehicleType vehicleType2 = new VehicleType();
        vehicleType2.setId(vehicleType1.getId());
        assertThat(vehicleType1).isEqualTo(vehicleType2);
        vehicleType2.setId(2L);
        assertThat(vehicleType1).isNotEqualTo(vehicleType2);
        vehicleType1.setId(null);
        assertThat(vehicleType1).isNotEqualTo(vehicleType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleTypeDTO.class);
        VehicleTypeDTO vehicleTypeDTO1 = new VehicleTypeDTO();
        vehicleTypeDTO1.setId(1L);
        VehicleTypeDTO vehicleTypeDTO2 = new VehicleTypeDTO();
        assertThat(vehicleTypeDTO1).isNotEqualTo(vehicleTypeDTO2);
        vehicleTypeDTO2.setId(vehicleTypeDTO1.getId());
        assertThat(vehicleTypeDTO1).isEqualTo(vehicleTypeDTO2);
        vehicleTypeDTO2.setId(2L);
        assertThat(vehicleTypeDTO1).isNotEqualTo(vehicleTypeDTO2);
        vehicleTypeDTO1.setId(null);
        assertThat(vehicleTypeDTO1).isNotEqualTo(vehicleTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(vehicleTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(vehicleTypeMapper.fromId(null)).isNull();
    }
}
