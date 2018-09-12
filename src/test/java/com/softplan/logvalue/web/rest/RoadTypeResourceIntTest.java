package com.softplan.logvalue.web.rest;

import com.softplan.logvalue.LogValueApp;

import com.softplan.logvalue.domain.RoadType;
import com.softplan.logvalue.repository.RoadTypeRepository;
import com.softplan.logvalue.service.RoadTypeService;
import com.softplan.logvalue.service.dto.RoadTypeDTO;
import com.softplan.logvalue.service.mapper.RoadTypeMapper;
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
 * Test class for the RoadTypeResource REST controller.
 *
 * @see RoadTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogValueApp.class)
public class RoadTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_FACTOR = 1F;
    private static final Float UPDATED_FACTOR = 2F;

    @Autowired
    private RoadTypeRepository roadTypeRepository;


    @Autowired
    private RoadTypeMapper roadTypeMapper;
    

    @Autowired
    private RoadTypeService roadTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRoadTypeMockMvc;

    private RoadType roadType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoadTypeResource roadTypeResource = new RoadTypeResource(roadTypeService);
        this.restRoadTypeMockMvc = MockMvcBuilders.standaloneSetup(roadTypeResource)
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
    public static RoadType createEntity(EntityManager em) {
        RoadType roadType = new RoadType()
            .name(DEFAULT_NAME)
            .factor(DEFAULT_FACTOR);
        return roadType;
    }

    @Before
    public void initTest() {
        roadType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoadType() throws Exception {
        int databaseSizeBeforeCreate = roadTypeRepository.findAll().size();

        // Create the RoadType
        RoadTypeDTO roadTypeDTO = roadTypeMapper.toDto(roadType);
        restRoadTypeMockMvc.perform(post("/api/road-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roadTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the RoadType in the database
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RoadType testRoadType = roadTypeList.get(roadTypeList.size() - 1);
        assertThat(testRoadType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoadType.getFactor()).isEqualTo(DEFAULT_FACTOR);
    }

    @Test
    @Transactional
    public void createRoadTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roadTypeRepository.findAll().size();

        // Create the RoadType with an existing ID
        roadType.setId(1L);
        RoadTypeDTO roadTypeDTO = roadTypeMapper.toDto(roadType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoadTypeMockMvc.perform(post("/api/road-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roadTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoadType in the database
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRoadTypes() throws Exception {
        // Initialize the database
        roadTypeRepository.saveAndFlush(roadType);

        // Get all the roadTypeList
        restRoadTypeMockMvc.perform(get("/api/road-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roadType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].factor").value(hasItem(DEFAULT_FACTOR.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getRoadType() throws Exception {
        // Initialize the database
        roadTypeRepository.saveAndFlush(roadType);

        // Get the roadType
        restRoadTypeMockMvc.perform(get("/api/road-types/{id}", roadType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roadType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.factor").value(DEFAULT_FACTOR.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingRoadType() throws Exception {
        // Get the roadType
        restRoadTypeMockMvc.perform(get("/api/road-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoadType() throws Exception {
        // Initialize the database
        roadTypeRepository.saveAndFlush(roadType);

        int databaseSizeBeforeUpdate = roadTypeRepository.findAll().size();

        // Update the roadType
        RoadType updatedRoadType = roadTypeRepository.findById(roadType.getId()).get();
        // Disconnect from session so that the updates on updatedRoadType are not directly saved in db
        em.detach(updatedRoadType);
        updatedRoadType
            .name(UPDATED_NAME)
            .factor(UPDATED_FACTOR);
        RoadTypeDTO roadTypeDTO = roadTypeMapper.toDto(updatedRoadType);

        restRoadTypeMockMvc.perform(put("/api/road-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roadTypeDTO)))
            .andExpect(status().isOk());

        // Validate the RoadType in the database
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeUpdate);
        RoadType testRoadType = roadTypeList.get(roadTypeList.size() - 1);
        assertThat(testRoadType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoadType.getFactor()).isEqualTo(UPDATED_FACTOR);
    }

    @Test
    @Transactional
    public void updateNonExistingRoadType() throws Exception {
        int databaseSizeBeforeUpdate = roadTypeRepository.findAll().size();

        // Create the RoadType
        RoadTypeDTO roadTypeDTO = roadTypeMapper.toDto(roadType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRoadTypeMockMvc.perform(put("/api/road-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roadTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoadType in the database
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoadType() throws Exception {
        // Initialize the database
        roadTypeRepository.saveAndFlush(roadType);

        int databaseSizeBeforeDelete = roadTypeRepository.findAll().size();

        // Get the roadType
        restRoadTypeMockMvc.perform(delete("/api/road-types/{id}", roadType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoadType> roadTypeList = roadTypeRepository.findAll();
        assertThat(roadTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoadType.class);
        RoadType roadType1 = new RoadType();
        roadType1.setId(1L);
        RoadType roadType2 = new RoadType();
        roadType2.setId(roadType1.getId());
        assertThat(roadType1).isEqualTo(roadType2);
        roadType2.setId(2L);
        assertThat(roadType1).isNotEqualTo(roadType2);
        roadType1.setId(null);
        assertThat(roadType1).isNotEqualTo(roadType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoadTypeDTO.class);
        RoadTypeDTO roadTypeDTO1 = new RoadTypeDTO();
        roadTypeDTO1.setId(1L);
        RoadTypeDTO roadTypeDTO2 = new RoadTypeDTO();
        assertThat(roadTypeDTO1).isNotEqualTo(roadTypeDTO2);
        roadTypeDTO2.setId(roadTypeDTO1.getId());
        assertThat(roadTypeDTO1).isEqualTo(roadTypeDTO2);
        roadTypeDTO2.setId(2L);
        assertThat(roadTypeDTO1).isNotEqualTo(roadTypeDTO2);
        roadTypeDTO1.setId(null);
        assertThat(roadTypeDTO1).isNotEqualTo(roadTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roadTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roadTypeMapper.fromId(null)).isNull();
    }
}
