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
 * Test class for the EstimateResource REST controller, 
 * specifically the calculation.
 *
 * @see EstimateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogValueApp.class)
public class EstimateResourceCalcIntTest {

    private static final Integer DEFAULT_PAVED_HIGHWAY_AMOUNT = 0;
    private static final Integer UPDATED_PAVED_HIGHWAY_AMOUNT = 1;

    private static final Integer DEFAULT_NON_PAVED_HIGHWAY_AMOUNT = 0;
    private static final Integer UPDATED_NON_PAVED_HIGHWAY_AMOUNT = 2;

    private static final Boolean DEFAULT_CONTAINS_TOLL = false;
    private static final Boolean UPDATED_CONTAINS_TOLL = true;

    private static final Float DEFAULT_TOLL_VALUE = 0F;
    private static final Float UPDATED_TOLL_VALUE = 1F;
    
    //private static final Float DEFAULT_FREIGHT_VALUE = 1F;
    //private static final Float UPDATED_FREIGHT_VALUE = 2F;
    
    private static final Long DEFAULT_OWNER_VALUE = 1L;
    private static final Long UPDATED_OWNRE_VALUE = 2L;
    
    private static final Long DEFAULT_VEHICLE_VALUE = 1L;
    private static final Long UPDATED_VEHICLE_VALUE = 2L;
    
    private static final Long BAU_VEHICLE_VALUE = 1L;
    private static final Long CACAMBA_VEHICLE_VALUE = 2L;
    private static final Long CARRETA_VEHICLE_VALUE = 3L;

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
            //.freightAmount(DEFAULT_FREIGHT_VALUE)
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
    public void case01() throws Exception {

        prepareLoggedUser();
        
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        estimate.vehicleType(new VehicleType(CACAMBA_VEHICLE_VALUE))
                .pavedHighwayAmount(100)
                .loadAmount(8);
                
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
        assertThat(testEstimate.getPavedHighwayAmount()).isEqualTo(100);
        
        assertThat(testEstimate.getFreightAmount()).isEqualTo(new Float(62.70));
    }
    
    
    @Test
    @Transactional
    public void case02() throws Exception {

        prepareLoggedUser();
        
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        estimate.vehicleType(new VehicleType(BAU_VEHICLE_VALUE))
                .pavedHighwayAmount(0)
                .nonPavedHighwayAmount(60)
                .loadAmount(4);
                
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
        
        assertThat(testEstimate.getFreightAmount()).isEqualTo(new Float(37.20));
    }
    
    @Test
    @Transactional
    public void case03() throws Exception {

        prepareLoggedUser();
        
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        estimate.vehicleType(new VehicleType(CARRETA_VEHICLE_VALUE))
                .pavedHighwayAmount(0)
                .nonPavedHighwayAmount(180)
                .loadAmount(12);
                
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
        
        assertThat(testEstimate.getFreightAmount()).isEqualTo(new Float(150.19));
    }
    
    @Test
    @Transactional
    public void case04() throws Exception {

        prepareLoggedUser();
        
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        estimate.vehicleType(new VehicleType(BAU_VEHICLE_VALUE))
                .pavedHighwayAmount(80)
                .nonPavedHighwayAmount(20)
                .loadAmount(6);
                
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
        
        assertThat(testEstimate.getFreightAmount()).isEqualTo(new Float(57.60));
    }
    
    @Test
    @Transactional
    public void case05() throws Exception {

        prepareLoggedUser();
        
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        estimate.vehicleType(new VehicleType(CACAMBA_VEHICLE_VALUE))
                .pavedHighwayAmount(50)
                .nonPavedHighwayAmount(30)
                .loadAmount(5);
                
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
        
        assertThat(testEstimate.getFreightAmount()).isEqualTo(new Float(47.88));
    }
    
    @Test
    @Transactional
    public void case06() throws Exception {

        prepareLoggedUser();
        
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        estimate.vehicleType(new VehicleType(CACAMBA_VEHICLE_VALUE))
                .pavedHighwayAmount(100)
                .loadAmount(8);
                
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
        assertThat(testEstimate.getPavedHighwayAmount()).isEqualTo(100);
        
        assertThat(testEstimate.getFreightAmount()).isEqualTo(new Float(62.70));
    }
    
    @Test
    @Transactional
    public void case07() throws Exception {

        prepareLoggedUser();
        
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        estimate.vehicleType(new VehicleType(CACAMBA_VEHICLE_VALUE))
                .pavedHighwayAmount(100)
                .loadAmount(8);
                
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
        assertThat(testEstimate.getPavedHighwayAmount()).isEqualTo(100);
        
        assertThat(testEstimate.getFreightAmount()).isEqualTo(new Float(62.70));
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
