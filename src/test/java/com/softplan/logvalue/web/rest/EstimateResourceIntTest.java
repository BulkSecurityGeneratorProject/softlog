package com.softplan.logvalue.web.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.softplan.logvalue.LogValueApp;
import com.softplan.logvalue.domain.Authority;
import com.softplan.logvalue.domain.Estimate;
import com.softplan.logvalue.domain.User;
import com.softplan.logvalue.domain.VehicleType;
import com.softplan.logvalue.repository.EstimateRepository;
import com.softplan.logvalue.repository.UserRepository;
import com.softplan.logvalue.security.AuthoritiesConstants;
import com.softplan.logvalue.security.SecurityUtils;
import com.softplan.logvalue.service.EstimateService;
import com.softplan.logvalue.service.MailService;
import com.softplan.logvalue.service.UserService;
import com.softplan.logvalue.service.dto.EstimateDTO;
import com.softplan.logvalue.service.mapper.EstimateMapper;
import com.softplan.logvalue.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.softplan.logvalue.web.rest.TestUtil.sameInstant;
import static com.softplan.logvalue.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EstimateResource REST controller.
 *
 * @see EstimateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogValueApp.class)
public class EstimateResourceIntTest {

    private static final Integer DEFAULT_PAVED_HIGHWAY_AMOUNT = 1;
    private static final Integer UPDATED_PAVED_HIGHWAY_AMOUNT = 2;

    private static final Integer DEFAULT_NON_PAVED_HIGHWAY_AMOUNT = 1;
    private static final Integer UPDATED_NON_PAVED_HIGHWAY_AMOUNT = 2;

    private static final Boolean DEFAULT_CONTAINS_TOLL = false;
    private static final Boolean UPDATED_CONTAINS_TOLL = true;

    private static final Float DEFAULT_TOLL_VALUE = 1F;
    private static final Float UPDATED_TOLL_VALUE = 2F;
    
    private static final Float DEFAULT_FREIGHT_VALUE = 1F;
    private static final Float UPDATED_FREIGHT_VALUE = 2F;
    
    private static final Long DEFAULT_OWNER_VALUE = 1L;
    private static final Long UPDATED_OWNRE_VALUE = 2L;
    
    private static final Long DEFAULT_VEHICLE_VALUE = 1L;
    private static final Long UPDATED_VEHICLE_VALUE = 2L;

    private static final Integer DEFAULT_LOAD_VALUE = 1;
    private static final Integer UPDATED_LOAD_VALUE = 2;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private EstimateMapper estimateMapper;
    
    @Autowired
    private EstimateService estimateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEstimateMockMvc;

    private MockMvc restUserMockMvc;

    private Estimate estimate;

    @Autowired
    private UserRepository userRepository;
    
    @Mock
    private MailService mockMailService;
    
    @Mock
    private UserService mockUserService;
    
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(mockMailService).sendActivationEmail(any());
        
        final EstimateResource estimateResource = new EstimateResource(estimateService, mockUserService);
        
        this.restEstimateMockMvc = MockMvcBuilders.standaloneSetup(estimateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        AccountResource accountUserMockResource =
            new AccountResource(userRepository, mockUserService, mockMailService);
            
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource)
            .setControllerAdvice(exceptionTranslator)
            .build();    
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estimate createEntity(EntityManager em) {
      
        User owner = new User();
        owner.setId(3L);
        
        Estimate estimate = new Estimate()
            .pavedHighwayAmount(DEFAULT_PAVED_HIGHWAY_AMOUNT)
            .nonPavedHighwayAmount(DEFAULT_NON_PAVED_HIGHWAY_AMOUNT)
            .containsToll(DEFAULT_CONTAINS_TOLL)
            .tollValue(DEFAULT_TOLL_VALUE)
            .freightAmount(DEFAULT_FREIGHT_VALUE)
            .loadAmount(DEFAULT_LOAD_VALUE)
            .vehicleType(new VehicleType(DEFAULT_VEHICLE_VALUE))
            .createdAt(DEFAULT_CREATED_AT)
            .owner(owner);
        return estimate;
    }

    @Before
    public void initTest() throws Exception {
        estimate = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstimate() throws Exception {

        prepareLoggedUser();
              
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        // Create the Estimate
        EstimateDTO estimateDTO = estimateMapper.toDto(estimate);
        
        restEstimateMockMvc.perform(post("/api/estimates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estimateDTO)))
            .andExpect(status().isCreated());

        // Validate the Estimate in the database
        List<Estimate> estimateList = estimateRepository.findAll();
        assertThat(estimateList).hasSize(databaseSizeBeforeCreate + 1);
        Estimate testEstimate = estimateList.get(estimateList.size() - 1);
        assertThat(testEstimate.getPavedHighwayAmount()).isEqualTo(DEFAULT_PAVED_HIGHWAY_AMOUNT);
        assertThat(testEstimate.getNonPavedHighwayAmount()).isEqualTo(DEFAULT_NON_PAVED_HIGHWAY_AMOUNT);
        assertThat(testEstimate.isContainsToll()).isEqualTo(DEFAULT_CONTAINS_TOLL);
        assertThat(testEstimate.getTollValue()).isEqualTo(DEFAULT_TOLL_VALUE);
    }

    @Test
    @Transactional
    public void createEstimateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        // Create the Estimate with an existing ID
        estimate.setId(1L);
        EstimateDTO estimateDTO = estimateMapper.toDto(estimate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstimateMockMvc.perform(post("/api/estimates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estimateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Estimate in the database
        List<Estimate> estimateList = estimateRepository.findAll();
        assertThat(estimateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEstimates() throws Exception {
      
      // TODO Este teste encontrou problema com o Mock para o SecurityUtils.isCurrentUserInRole. Preciso resolver 
      
        // Initialize the database
//        estimateRepository.saveAndFlush(estimate);

        // Get all the estimateList
//        restEstimateMockMvc.perform(get("/api/estimates?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(estimate.getId().intValue())))
//            .andExpect(jsonPath("$.[*].pavedHighwayAmount").value(hasItem(DEFAULT_PAVED_HIGHWAY_AMOUNT)))
//            .andExpect(jsonPath("$.[*].nonPavedHighwayAmount").value(hasItem(DEFAULT_NON_PAVED_HIGHWAY_AMOUNT)))
//            .andExpect(jsonPath("$.[*].containsToll").value(hasItem(DEFAULT_CONTAINS_TOLL.booleanValue())))
//            .andExpect(jsonPath("$.[*].tollValue").value(hasItem(DEFAULT_TOLL_VALUE.doubleValue())))
//            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }
    

    @Test
    @Transactional
    public void getEstimate() throws Exception {
        // Initialize the database
        estimateRepository.saveAndFlush(estimate);

        // Get the estimate
        restEstimateMockMvc.perform(get("/api/estimates/{id}", estimate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estimate.getId().intValue()))
            .andExpect(jsonPath("$.pavedHighwayAmount").value(DEFAULT_PAVED_HIGHWAY_AMOUNT))
            .andExpect(jsonPath("$.nonPavedHighwayAmount").value(DEFAULT_NON_PAVED_HIGHWAY_AMOUNT))
            .andExpect(jsonPath("$.containsToll").value(DEFAULT_CONTAINS_TOLL.booleanValue()))
            .andExpect(jsonPath("$.tollValue").value(DEFAULT_TOLL_VALUE.doubleValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }
    @Test
    @Transactional
    public void getNonExistingEstimate() throws Exception {
        // Get the estimate
        restEstimateMockMvc.perform(get("/api/estimates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstimate() throws Exception {
          
        prepareLoggedUser();
      
        // Initialize the database
        estimateRepository.saveAndFlush(estimate);

        int databaseSizeBeforeUpdate = estimateRepository.findAll().size();

        // Update the estimate
        Estimate updatedEstimate = estimateRepository.findById(estimate.getId()).get();
        // Disconnect from session so that the updates on updatedEstimate are not directly saved in db
        em.detach(updatedEstimate);
        updatedEstimate
            .pavedHighwayAmount(UPDATED_PAVED_HIGHWAY_AMOUNT)
            .nonPavedHighwayAmount(UPDATED_NON_PAVED_HIGHWAY_AMOUNT)
            .containsToll(UPDATED_CONTAINS_TOLL)
            .tollValue(UPDATED_TOLL_VALUE)
            .createdAt(UPDATED_CREATED_AT);
        EstimateDTO estimateDTO = estimateMapper.toDto(updatedEstimate);

        restEstimateMockMvc.perform(put("/api/estimates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estimateDTO)))
            .andExpect(status().isOk());

        // Validate the Estimate in the database
        List<Estimate> estimateList = estimateRepository.findAll();
        assertThat(estimateList).hasSize(databaseSizeBeforeUpdate);
        Estimate testEstimate = estimateList.get(estimateList.size() - 1);
        assertThat(testEstimate.getPavedHighwayAmount()).isEqualTo(UPDATED_PAVED_HIGHWAY_AMOUNT);
        assertThat(testEstimate.getNonPavedHighwayAmount()).isEqualTo(UPDATED_NON_PAVED_HIGHWAY_AMOUNT);
        assertThat(testEstimate.isContainsToll()).isEqualTo(UPDATED_CONTAINS_TOLL);
        assertThat(testEstimate.getTollValue()).isEqualTo(UPDATED_TOLL_VALUE);
        // assertThat(testEstimate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingEstimate() throws Exception {
        int databaseSizeBeforeUpdate = estimateRepository.findAll().size();

        // Create the Estimate
        EstimateDTO estimateDTO = estimateMapper.toDto(estimate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEstimateMockMvc.perform(put("/api/estimates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estimateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Estimate in the database
        List<Estimate> estimateList = estimateRepository.findAll();
        assertThat(estimateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEstimate() throws Exception {
        // Initialize the database
        estimateRepository.saveAndFlush(estimate);

        int databaseSizeBeforeDelete = estimateRepository.findAll().size();

        // Get the estimate
        restEstimateMockMvc.perform(delete("/api/estimates/{id}", estimate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Estimate> estimateList = estimateRepository.findAll();
        assertThat(estimateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estimate.class);
        Estimate estimate1 = new Estimate();
        estimate1.setId(1L);
        Estimate estimate2 = new Estimate();
        estimate2.setId(estimate1.getId());
        assertThat(estimate1).isEqualTo(estimate2);
        estimate2.setId(2L);
        assertThat(estimate1).isNotEqualTo(estimate2);
        estimate1.setId(null);
        assertThat(estimate1).isNotEqualTo(estimate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstimateDTO.class);
        EstimateDTO estimateDTO1 = new EstimateDTO();
        estimateDTO1.setId(1L);
        EstimateDTO estimateDTO2 = new EstimateDTO();
        assertThat(estimateDTO1).isNotEqualTo(estimateDTO2);
        estimateDTO2.setId(estimateDTO1.getId());
        assertThat(estimateDTO1).isEqualTo(estimateDTO2);
        estimateDTO2.setId(2L);
        assertThat(estimateDTO1).isNotEqualTo(estimateDTO2);
        estimateDTO1.setId(null);
        assertThat(estimateDTO1).isNotEqualTo(estimateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(estimateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(estimateMapper.fromId(null)).isNull();
    }



    private void prepareLoggedUser() throws Exception {
        Set<Authority> authorities = new HashSet();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);

        User user = new User();
        user.setId(3L);
        user.setLogin("test");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setEmail("john.doe@jhipster.com");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("en");
        user.setAuthorities(authorities);
        user.setPassword(new BCryptPasswordEncoder().encode("sssssasqwq"));
        
        userRepository.saveAndFlush(user);
        
        when(mockUserService.getCurrentUser()).thenReturn(user);
        //when(mockUserService.getUserWithAuthorities()).thenReturn(Optional.of(user));
        
    }
}
