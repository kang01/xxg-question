package org.fwoxford.web.rest;

import org.fwoxford.MisBbisQuestionApp;

import org.fwoxford.domain.SendRecord;
import org.fwoxford.repository.SendRecordRepository;
import org.fwoxford.service.SendRecordService;
import org.fwoxford.service.dto.SendRecordDTO;
import org.fwoxford.service.mapper.SendRecordMapper;
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
import java.util.List;

import static org.fwoxford.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SendRecordResource REST controller.
 *
 * @see SendRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisBbisQuestionApp.class)
public class SendRecordResourceIntTest {

    private static final String DEFAULT_SENDER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_STRANGER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_STRANGER_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_QUESTION_ID = 1L;
    private static final Long UPDATED_QUESTION_ID = 2L;

    private static final String DEFAULT_QUESTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_AUTHORIZATION_RECORD_ID = 1L;
    private static final Long UPDATED_AUTHORIZATION_RECORD_ID = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private SendRecordRepository sendRecordRepository;

    @Autowired
    private SendRecordMapper sendRecordMapper;

    @Autowired
    private SendRecordService sendRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSendRecordMockMvc;

    private SendRecord sendRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SendRecordResource sendRecordResource = new SendRecordResource(sendRecordService);
        this.restSendRecordMockMvc = MockMvcBuilders.standaloneSetup(sendRecordResource)
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
    public static SendRecord createEntity(EntityManager em) {
        SendRecord sendRecord = new SendRecord()
            .senderEmail(DEFAULT_SENDER_EMAIL)
            .strangerEmail(DEFAULT_STRANGER_EMAIL)
            .questionId(DEFAULT_QUESTION_ID)
            .questionCode(DEFAULT_QUESTION_CODE)
            .authorizationRecordId(DEFAULT_AUTHORIZATION_RECORD_ID)
            .status(DEFAULT_STATUS)
            .memo(DEFAULT_MEMO);
        return sendRecord;
    }

    @Before
    public void initTest() {
        sendRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createSendRecord() throws Exception {
        int databaseSizeBeforeCreate = sendRecordRepository.findAll().size();

        // Create the SendRecord
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);
        restSendRecordMockMvc.perform(post("/api/send-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the SendRecord in the database
        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeCreate + 1);
        SendRecord testSendRecord = sendRecordList.get(sendRecordList.size() - 1);
        assertThat(testSendRecord.getSenderEmail()).isEqualTo(DEFAULT_SENDER_EMAIL);
        assertThat(testSendRecord.getStrangerEmail()).isEqualTo(DEFAULT_STRANGER_EMAIL);
        assertThat(testSendRecord.getQuestionId()).isEqualTo(DEFAULT_QUESTION_ID);
        assertThat(testSendRecord.getQuestionCode()).isEqualTo(DEFAULT_QUESTION_CODE);
        assertThat(testSendRecord.getAuthorizationRecordId()).isEqualTo(DEFAULT_AUTHORIZATION_RECORD_ID);
        assertThat(testSendRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSendRecord.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createSendRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sendRecordRepository.findAll().size();

        // Create the SendRecord with an existing ID
        sendRecord.setId(1L);
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSendRecordMockMvc.perform(post("/api/send-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SendRecord in the database
        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSenderEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = sendRecordRepository.findAll().size();
        // set the field null
        sendRecord.setSenderEmail(null);

        // Create the SendRecord, which fails.
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);

        restSendRecordMockMvc.perform(post("/api/send-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendRecordDTO)))
            .andExpect(status().isBadRequest());

        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStrangerEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = sendRecordRepository.findAll().size();
        // set the field null
        sendRecord.setStrangerEmail(null);

        // Create the SendRecord, which fails.
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);

        restSendRecordMockMvc.perform(post("/api/send-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendRecordDTO)))
            .andExpect(status().isBadRequest());

        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sendRecordRepository.findAll().size();
        // set the field null
        sendRecord.setQuestionId(null);

        // Create the SendRecord, which fails.
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);

        restSendRecordMockMvc.perform(post("/api/send-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendRecordDTO)))
            .andExpect(status().isBadRequest());

        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestionCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sendRecordRepository.findAll().size();
        // set the field null
        sendRecord.setQuestionCode(null);

        // Create the SendRecord, which fails.
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);

        restSendRecordMockMvc.perform(post("/api/send-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendRecordDTO)))
            .andExpect(status().isBadRequest());

        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSendRecords() throws Exception {
        // Initialize the database
        sendRecordRepository.saveAndFlush(sendRecord);

        // Get all the sendRecordList
        restSendRecordMockMvc.perform(get("/api/send-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sendRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].senderEmail").value(hasItem(DEFAULT_SENDER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].strangerEmail").value(hasItem(DEFAULT_STRANGER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].questionId").value(hasItem(DEFAULT_QUESTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].questionCode").value(hasItem(DEFAULT_QUESTION_CODE.toString())))
            .andExpect(jsonPath("$.[*].authorizationRecordId").value(hasItem(DEFAULT_AUTHORIZATION_RECORD_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getSendRecord() throws Exception {
        // Initialize the database
        sendRecordRepository.saveAndFlush(sendRecord);

        // Get the sendRecord
        restSendRecordMockMvc.perform(get("/api/send-records/{id}", sendRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sendRecord.getId().intValue()))
            .andExpect(jsonPath("$.senderEmail").value(DEFAULT_SENDER_EMAIL.toString()))
            .andExpect(jsonPath("$.strangerEmail").value(DEFAULT_STRANGER_EMAIL.toString()))
            .andExpect(jsonPath("$.questionId").value(DEFAULT_QUESTION_ID.intValue()))
            .andExpect(jsonPath("$.questionCode").value(DEFAULT_QUESTION_CODE.toString()))
            .andExpect(jsonPath("$.authorizationRecordId").value(DEFAULT_AUTHORIZATION_RECORD_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSendRecord() throws Exception {
        // Get the sendRecord
        restSendRecordMockMvc.perform(get("/api/send-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSendRecord() throws Exception {
        // Initialize the database
        sendRecordRepository.saveAndFlush(sendRecord);
        int databaseSizeBeforeUpdate = sendRecordRepository.findAll().size();

        // Update the sendRecord
        SendRecord updatedSendRecord = sendRecordRepository.findOne(sendRecord.getId());
        // Disconnect from session so that the updates on updatedSendRecord are not directly saved in db
        em.detach(updatedSendRecord);
        updatedSendRecord
            .senderEmail(UPDATED_SENDER_EMAIL)
            .strangerEmail(UPDATED_STRANGER_EMAIL)
            .questionId(UPDATED_QUESTION_ID)
            .questionCode(UPDATED_QUESTION_CODE)
            .authorizationRecordId(UPDATED_AUTHORIZATION_RECORD_ID)
            .status(UPDATED_STATUS)
            .memo(UPDATED_MEMO);
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(updatedSendRecord);

        restSendRecordMockMvc.perform(put("/api/send-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendRecordDTO)))
            .andExpect(status().isOk());

        // Validate the SendRecord in the database
        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeUpdate);
        SendRecord testSendRecord = sendRecordList.get(sendRecordList.size() - 1);
        assertThat(testSendRecord.getSenderEmail()).isEqualTo(UPDATED_SENDER_EMAIL);
        assertThat(testSendRecord.getStrangerEmail()).isEqualTo(UPDATED_STRANGER_EMAIL);
        assertThat(testSendRecord.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testSendRecord.getQuestionCode()).isEqualTo(UPDATED_QUESTION_CODE);
        assertThat(testSendRecord.getAuthorizationRecordId()).isEqualTo(UPDATED_AUTHORIZATION_RECORD_ID);
        assertThat(testSendRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSendRecord.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingSendRecord() throws Exception {
        int databaseSizeBeforeUpdate = sendRecordRepository.findAll().size();

        // Create the SendRecord
        SendRecordDTO sendRecordDTO = sendRecordMapper.sendRecordToSendRecordDTO(sendRecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSendRecordMockMvc.perform(put("/api/send-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sendRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the SendRecord in the database
        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSendRecord() throws Exception {
        // Initialize the database
        sendRecordRepository.saveAndFlush(sendRecord);
        int databaseSizeBeforeDelete = sendRecordRepository.findAll().size();

        // Get the sendRecord
        restSendRecordMockMvc.perform(delete("/api/send-records/{id}", sendRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SendRecord> sendRecordList = sendRecordRepository.findAll();
        assertThat(sendRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SendRecord.class);
        SendRecord sendRecord1 = new SendRecord();
        sendRecord1.setId(1L);
        SendRecord sendRecord2 = new SendRecord();
        sendRecord2.setId(sendRecord1.getId());
        assertThat(sendRecord1).isEqualTo(sendRecord2);
        sendRecord2.setId(2L);
        assertThat(sendRecord1).isNotEqualTo(sendRecord2);
        sendRecord1.setId(null);
        assertThat(sendRecord1).isNotEqualTo(sendRecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SendRecordDTO.class);
        SendRecordDTO sendRecordDTO1 = new SendRecordDTO();
        sendRecordDTO1.setId(1L);
        SendRecordDTO sendRecordDTO2 = new SendRecordDTO();
        assertThat(sendRecordDTO1).isNotEqualTo(sendRecordDTO2);
        sendRecordDTO2.setId(sendRecordDTO1.getId());
        assertThat(sendRecordDTO1).isEqualTo(sendRecordDTO2);
        sendRecordDTO2.setId(2L);
        assertThat(sendRecordDTO1).isNotEqualTo(sendRecordDTO2);
        sendRecordDTO1.setId(null);
        assertThat(sendRecordDTO1).isNotEqualTo(sendRecordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sendRecordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sendRecordMapper.fromId(null)).isNull();
    }
}
