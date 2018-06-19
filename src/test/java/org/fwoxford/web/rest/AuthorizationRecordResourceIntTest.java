package org.fwoxford.web.rest;

import org.fwoxford.MisBbisQuestionApp;

import org.fwoxford.domain.AuthorizationRecord;
import org.fwoxford.repository.AuthorizationRecordRepository;
import org.fwoxford.service.AuthorizationRecordService;
import org.fwoxford.service.dto.AuthorizationRecordDTO;
import org.fwoxford.service.mapper.AuthorizationRecordMapper;
import org.fwoxford.web.rest.errors.ExceptionTranslator;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.fwoxford.web.rest.TestUtil.sameInstant;
import static org.fwoxford.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AuthorizationRecordResource REST controller.
 *
 * @see AuthorizationRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisBbisQuestionApp.class)
public class AuthorizationRecordResourceIntTest {

    private static final String DEFAULT_AUTHORIZATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORIZATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STRANGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STRANGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STRANGER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_STRANGER_EMAIL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_APPLY_TIMES = 10;
    private static final Integer UPDATED_APPLY_TIMES = 9;

    private static final Long DEFAULT_AUTHORITY_PERSON_ID = 1L;
    private static final Long UPDATED_AUTHORITY_PERSON_ID = 2L;

    private static final String DEFAULT_AUTHORITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private AuthorizationRecordRepository authorizationRecordRepository;

    @Autowired
    private AuthorizationRecordMapper authorizationRecordMapper;

    @Autowired
    private AuthorizationRecordService authorizationRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAuthorizationRecordMockMvc;

    private AuthorizationRecord authorizationRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthorizationRecordResource authorizationRecordResource = new AuthorizationRecordResource(authorizationRecordService);
        this.restAuthorizationRecordMockMvc = MockMvcBuilders.standaloneSetup(authorizationRecordResource)
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
    public static AuthorizationRecord createEntity(EntityManager em) {
        AuthorizationRecord authorizationRecord = new AuthorizationRecord()
            .authorizationCode(DEFAULT_AUTHORIZATION_CODE)
            .strangerName(DEFAULT_STRANGER_NAME)
            .strangerEmail(DEFAULT_STRANGER_EMAIL)
            .expirationTime(DEFAULT_EXPIRATION_TIME)
            .applyTimes(DEFAULT_APPLY_TIMES)
            .authorityPersonId(DEFAULT_AUTHORITY_PERSON_ID)
            .authorityName(DEFAULT_AUTHORITY_NAME)
            .status(DEFAULT_STATUS)
            .memo(DEFAULT_MEMO);
        return authorizationRecord;
    }

    @Before
    public void initTest() {
        authorizationRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthorizationRecord() throws Exception {
        int databaseSizeBeforeCreate = authorizationRecordRepository.findAll().size();

        // Create the AuthorizationRecord
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);
        restAuthorizationRecordMockMvc.perform(post("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthorizationRecord in the database
        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeCreate + 1);
        AuthorizationRecord testAuthorizationRecord = authorizationRecordList.get(authorizationRecordList.size() - 1);
        assertThat(testAuthorizationRecord.getAuthorizationCode()).isEqualTo(DEFAULT_AUTHORIZATION_CODE);
        assertThat(testAuthorizationRecord.getStrangerName()).isEqualTo(DEFAULT_STRANGER_NAME);
        assertThat(testAuthorizationRecord.getStrangerEmail()).isEqualTo(DEFAULT_STRANGER_EMAIL);
        assertThat(testAuthorizationRecord.getExpirationTime()).isEqualTo(DEFAULT_EXPIRATION_TIME);
        assertThat(testAuthorizationRecord.getApplyTimes()).isEqualTo(DEFAULT_APPLY_TIMES);
        assertThat(testAuthorizationRecord.getAuthorityPersonId()).isEqualTo(DEFAULT_AUTHORITY_PERSON_ID);
        assertThat(testAuthorizationRecord.getAuthorityName()).isEqualTo(DEFAULT_AUTHORITY_NAME);
        assertThat(testAuthorizationRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAuthorizationRecord.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createAuthorizationRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorizationRecordRepository.findAll().size();

        // Create the AuthorizationRecord with an existing ID
        authorizationRecord.setId(1L);
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorizationRecordMockMvc.perform(post("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuthorizationRecord in the database
        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAuthorizationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRecordRepository.findAll().size();
        // set the field null
        authorizationRecord.setAuthorizationCode(null);

        // Create the AuthorizationRecord, which fails.
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);

        restAuthorizationRecordMockMvc.perform(post("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStrangerEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRecordRepository.findAll().size();
        // set the field null
        authorizationRecord.setStrangerEmail(null);

        // Create the AuthorizationRecord, which fails.
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);

        restAuthorizationRecordMockMvc.perform(post("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRecordRepository.findAll().size();
        // set the field null
        authorizationRecord.setExpirationTime(null);

        // Create the AuthorizationRecord, which fails.
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);

        restAuthorizationRecordMockMvc.perform(post("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorityPersonIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRecordRepository.findAll().size();
        // set the field null
        authorizationRecord.setAuthorityPersonId(null);

        // Create the AuthorizationRecord, which fails.
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);

        restAuthorizationRecordMockMvc.perform(post("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRecordRepository.findAll().size();
        // set the field null
        authorizationRecord.setAuthorityName(null);

        // Create the AuthorizationRecord, which fails.
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);

        restAuthorizationRecordMockMvc.perform(post("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRecordRepository.findAll().size();
        // set the field null
        authorizationRecord.setStatus(null);

        // Create the AuthorizationRecord, which fails.
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);

        restAuthorizationRecordMockMvc.perform(post("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuthorizationRecords() throws Exception {
        // Initialize the database
        authorizationRecordRepository.saveAndFlush(authorizationRecord);

        // Get all the authorizationRecordList
        restAuthorizationRecordMockMvc.perform(get("/api/authorization-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorizationRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].authorizationCode").value(hasItem(DEFAULT_AUTHORIZATION_CODE.toString())))
            .andExpect(jsonPath("$.[*].strangerName").value(hasItem(DEFAULT_STRANGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].strangerEmail").value(hasItem(DEFAULT_STRANGER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].expirationTime").value(hasItem(sameInstant(DEFAULT_EXPIRATION_TIME))))
            .andExpect(jsonPath("$.[*].applyTimes").value(hasItem(DEFAULT_APPLY_TIMES)))
            .andExpect(jsonPath("$.[*].authorityPersonId").value(hasItem(DEFAULT_AUTHORITY_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].authorityName").value(hasItem(DEFAULT_AUTHORITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getAuthorizationRecord() throws Exception {
        // Initialize the database
        authorizationRecordRepository.saveAndFlush(authorizationRecord);

        // Get the authorizationRecord
        restAuthorizationRecordMockMvc.perform(get("/api/authorization-records/{id}", authorizationRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authorizationRecord.getId().intValue()))
            .andExpect(jsonPath("$.authorizationCode").value(DEFAULT_AUTHORIZATION_CODE.toString()))
            .andExpect(jsonPath("$.strangerName").value(DEFAULT_STRANGER_NAME.toString()))
            .andExpect(jsonPath("$.strangerEmail").value(DEFAULT_STRANGER_EMAIL.toString()))
            .andExpect(jsonPath("$.expirationTime").value(sameInstant(DEFAULT_EXPIRATION_TIME)))
            .andExpect(jsonPath("$.applyTimes").value(DEFAULT_APPLY_TIMES))
            .andExpect(jsonPath("$.authorityPersonId").value(DEFAULT_AUTHORITY_PERSON_ID.intValue()))
            .andExpect(jsonPath("$.authorityName").value(DEFAULT_AUTHORITY_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthorizationRecord() throws Exception {
        // Get the authorizationRecord
        restAuthorizationRecordMockMvc.perform(get("/api/authorization-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthorizationRecord() throws Exception {
        // Initialize the database
        authorizationRecordRepository.saveAndFlush(authorizationRecord);
        int databaseSizeBeforeUpdate = authorizationRecordRepository.findAll().size();

        // Update the authorizationRecord
        AuthorizationRecord updatedAuthorizationRecord = authorizationRecordRepository.findOne(authorizationRecord.getId());
        // Disconnect from session so that the updates on updatedAuthorizationRecord are not directly saved in db
        em.detach(updatedAuthorizationRecord);
        updatedAuthorizationRecord
            .authorizationCode(UPDATED_AUTHORIZATION_CODE)
            .strangerName(UPDATED_STRANGER_NAME)
            .strangerEmail(UPDATED_STRANGER_EMAIL)
            .expirationTime(UPDATED_EXPIRATION_TIME)
            .applyTimes(UPDATED_APPLY_TIMES)
            .authorityPersonId(UPDATED_AUTHORITY_PERSON_ID)
            .authorityName(UPDATED_AUTHORITY_NAME)
            .status(UPDATED_STATUS)
            .memo(UPDATED_MEMO);
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(updatedAuthorizationRecord);

        restAuthorizationRecordMockMvc.perform(put("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isOk());

        // Validate the AuthorizationRecord in the database
        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeUpdate);
        AuthorizationRecord testAuthorizationRecord = authorizationRecordList.get(authorizationRecordList.size() - 1);
        assertThat(testAuthorizationRecord.getAuthorizationCode()).isEqualTo(UPDATED_AUTHORIZATION_CODE);
        assertThat(testAuthorizationRecord.getStrangerName()).isEqualTo(UPDATED_STRANGER_NAME);
        assertThat(testAuthorizationRecord.getStrangerEmail()).isEqualTo(UPDATED_STRANGER_EMAIL);
        assertThat(testAuthorizationRecord.getExpirationTime()).isEqualTo(UPDATED_EXPIRATION_TIME);
        assertThat(testAuthorizationRecord.getApplyTimes()).isEqualTo(UPDATED_APPLY_TIMES);
        assertThat(testAuthorizationRecord.getAuthorityPersonId()).isEqualTo(UPDATED_AUTHORITY_PERSON_ID);
        assertThat(testAuthorizationRecord.getAuthorityName()).isEqualTo(UPDATED_AUTHORITY_NAME);
        assertThat(testAuthorizationRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAuthorizationRecord.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthorizationRecord() throws Exception {
        int databaseSizeBeforeUpdate = authorizationRecordRepository.findAll().size();

        // Create the AuthorizationRecord
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordMapper.authorizationRecordToAuthorizationRecordDTO(authorizationRecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAuthorizationRecordMockMvc.perform(put("/api/authorization-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizationRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthorizationRecord in the database
        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAuthorizationRecord() throws Exception {
        // Initialize the database
        authorizationRecordRepository.saveAndFlush(authorizationRecord);
        int databaseSizeBeforeDelete = authorizationRecordRepository.findAll().size();

        // Get the authorizationRecord
        restAuthorizationRecordMockMvc.perform(delete("/api/authorization-records/{id}", authorizationRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AuthorizationRecord> authorizationRecordList = authorizationRecordRepository.findAll();
        assertThat(authorizationRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorizationRecord.class);
        AuthorizationRecord authorizationRecord1 = new AuthorizationRecord();
        authorizationRecord1.setId(1L);
        AuthorizationRecord authorizationRecord2 = new AuthorizationRecord();
        authorizationRecord2.setId(authorizationRecord1.getId());
        assertThat(authorizationRecord1).isEqualTo(authorizationRecord2);
        authorizationRecord2.setId(2L);
        assertThat(authorizationRecord1).isNotEqualTo(authorizationRecord2);
        authorizationRecord1.setId(null);
        assertThat(authorizationRecord1).isNotEqualTo(authorizationRecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorizationRecordDTO.class);
        AuthorizationRecordDTO authorizationRecordDTO1 = new AuthorizationRecordDTO();
        authorizationRecordDTO1.setId(1L);
        AuthorizationRecordDTO authorizationRecordDTO2 = new AuthorizationRecordDTO();
        assertThat(authorizationRecordDTO1).isNotEqualTo(authorizationRecordDTO2);
        authorizationRecordDTO2.setId(authorizationRecordDTO1.getId());
        assertThat(authorizationRecordDTO1).isEqualTo(authorizationRecordDTO2);
        authorizationRecordDTO2.setId(2L);
        assertThat(authorizationRecordDTO1).isNotEqualTo(authorizationRecordDTO2);
        authorizationRecordDTO1.setId(null);
        assertThat(authorizationRecordDTO1).isNotEqualTo(authorizationRecordDTO2);
    }

//    @Test
//    @Transactional
//    public void testEntityFromId() {
//        assertThat(authorizationRecordMapper.fromId(42L).getId()).isEqualTo(42);
//        assertThat(authorizationRecordMapper.fromId(null)).isNull();
//    }
}
