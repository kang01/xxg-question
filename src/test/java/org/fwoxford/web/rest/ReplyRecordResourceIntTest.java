package org.fwoxford.web.rest;

import org.fwoxford.MisBbisQuestionApp;

import org.fwoxford.domain.ReplyRecord;
import org.fwoxford.domain.QuestionItemDetails;
import org.fwoxford.repository.ReplyRecordRepository;
import org.fwoxford.service.ReplyRecordService;
import org.fwoxford.service.dto.ReplyRecordDTO;
import org.fwoxford.service.mapper.ReplyRecordMapper;
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
 * Test class for the ReplyRecordResource REST controller.
 *
 * @see ReplyRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisBbisQuestionApp.class)
public class ReplyRecordResourceIntTest {

    private static final String DEFAULT_STRANGER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_STRANGER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_STRANGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STRANGER_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_QUESTION_ID = 1L;
    private static final Long UPDATED_QUESTION_ID = 2L;

    private static final String DEFAULT_QUESTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_HANDLE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_HANDLE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REPLY_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_REPLY_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private ReplyRecordRepository replyRecordRepository;

    @Autowired
    private ReplyRecordMapper replyRecordMapper;

    @Autowired
    private ReplyRecordService replyRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReplyRecordMockMvc;

    private ReplyRecord replyRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReplyRecordResource replyRecordResource = new ReplyRecordResource(replyRecordService);
        this.restReplyRecordMockMvc = MockMvcBuilders.standaloneSetup(replyRecordResource)
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
    public static ReplyRecord createEntity(EntityManager em) {
        ReplyRecord replyRecord = new ReplyRecord()
            .strangerEmail(DEFAULT_STRANGER_EMAIL)
            .strangerName(DEFAULT_STRANGER_NAME)
            .questionId(DEFAULT_QUESTION_ID)
            .questionCode(DEFAULT_QUESTION_CODE)
            .handleTypeCode(DEFAULT_HANDLE_TYPE_CODE)
            .replyContent(DEFAULT_REPLY_CONTENT)
            .status(DEFAULT_STATUS)
            .memo(DEFAULT_MEMO);
        // Add required entity
//        QuestionItemDetails questionItemDetails = QuestionItemDetailsResourceIntTest.createEntity(em);
//        em.persist(questionItemDetails);
//        em.flush();
//        replyRecord.setQuestionItemDetails(questionItemDetails);
        return replyRecord;
    }

    @Before
    public void initTest() {
        replyRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createReplyRecord() throws Exception {
        int databaseSizeBeforeCreate = replyRecordRepository.findAll().size();

        // Create the ReplyRecord
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);
        restReplyRecordMockMvc.perform(post("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the ReplyRecord in the database
        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeCreate + 1);
        ReplyRecord testReplyRecord = replyRecordList.get(replyRecordList.size() - 1);
        assertThat(testReplyRecord.getStrangerEmail()).isEqualTo(DEFAULT_STRANGER_EMAIL);
        assertThat(testReplyRecord.getStrangerName()).isEqualTo(DEFAULT_STRANGER_NAME);
        assertThat(testReplyRecord.getQuestionId()).isEqualTo(DEFAULT_QUESTION_ID);
        assertThat(testReplyRecord.getQuestionCode()).isEqualTo(DEFAULT_QUESTION_CODE);
        assertThat(testReplyRecord.getHandleTypeCode()).isEqualTo(DEFAULT_HANDLE_TYPE_CODE);
        assertThat(testReplyRecord.getReplyContent()).isEqualTo(DEFAULT_REPLY_CONTENT);
        assertThat(testReplyRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReplyRecord.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createReplyRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = replyRecordRepository.findAll().size();

        // Create the ReplyRecord with an existing ID
        replyRecord.setId(1L);
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReplyRecordMockMvc.perform(post("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReplyRecord in the database
        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStrangerEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyRecordRepository.findAll().size();
        // set the field null
        replyRecord.setStrangerEmail(null);

        // Create the ReplyRecord, which fails.
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);

        restReplyRecordMockMvc.perform(post("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyRecordRepository.findAll().size();
        // set the field null
        replyRecord.setQuestionId(null);

        // Create the ReplyRecord, which fails.
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);

        restReplyRecordMockMvc.perform(post("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestionCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyRecordRepository.findAll().size();
        // set the field null
        replyRecord.setQuestionCode(null);

        // Create the ReplyRecord, which fails.
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);

        restReplyRecordMockMvc.perform(post("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHandleTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyRecordRepository.findAll().size();
        // set the field null
        replyRecord.setHandleTypeCode(null);

        // Create the ReplyRecord, which fails.
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);

        restReplyRecordMockMvc.perform(post("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReplyContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyRecordRepository.findAll().size();
        // set the field null
        replyRecord.setReplyContent(null);

        // Create the ReplyRecord, which fails.
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);

        restReplyRecordMockMvc.perform(post("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyRecordRepository.findAll().size();
        // set the field null
        replyRecord.setStatus(null);

        // Create the ReplyRecord, which fails.
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);

        restReplyRecordMockMvc.perform(post("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReplyRecords() throws Exception {
        // Initialize the database
        replyRecordRepository.saveAndFlush(replyRecord);

        // Get all the replyRecordList
        restReplyRecordMockMvc.perform(get("/api/reply-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(replyRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].strangerEmail").value(hasItem(DEFAULT_STRANGER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].strangerName").value(hasItem(DEFAULT_STRANGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].questionId").value(hasItem(DEFAULT_QUESTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].questionCode").value(hasItem(DEFAULT_QUESTION_CODE.toString())))
            .andExpect(jsonPath("$.[*].handleTypeCode").value(hasItem(DEFAULT_HANDLE_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].replyContent").value(hasItem(DEFAULT_REPLY_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getReplyRecord() throws Exception {
        // Initialize the database
        replyRecordRepository.saveAndFlush(replyRecord);

        // Get the replyRecord
        restReplyRecordMockMvc.perform(get("/api/reply-records/{id}", replyRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(replyRecord.getId().intValue()))
            .andExpect(jsonPath("$.strangerEmail").value(DEFAULT_STRANGER_EMAIL.toString()))
            .andExpect(jsonPath("$.strangerName").value(DEFAULT_STRANGER_NAME.toString()))
            .andExpect(jsonPath("$.questionId").value(DEFAULT_QUESTION_ID.intValue()))
            .andExpect(jsonPath("$.questionCode").value(DEFAULT_QUESTION_CODE.toString()))
            .andExpect(jsonPath("$.handleTypeCode").value(DEFAULT_HANDLE_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.replyContent").value(DEFAULT_REPLY_CONTENT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReplyRecord() throws Exception {
        // Get the replyRecord
        restReplyRecordMockMvc.perform(get("/api/reply-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReplyRecord() throws Exception {
        // Initialize the database
        replyRecordRepository.saveAndFlush(replyRecord);
        int databaseSizeBeforeUpdate = replyRecordRepository.findAll().size();

        // Update the replyRecord
        ReplyRecord updatedReplyRecord = replyRecordRepository.findOne(replyRecord.getId());
        // Disconnect from session so that the updates on updatedReplyRecord are not directly saved in db
        em.detach(updatedReplyRecord);
        updatedReplyRecord
            .strangerEmail(UPDATED_STRANGER_EMAIL)
            .strangerName(UPDATED_STRANGER_NAME)
            .questionId(UPDATED_QUESTION_ID)
            .questionCode(UPDATED_QUESTION_CODE)
            .handleTypeCode(UPDATED_HANDLE_TYPE_CODE)
            .replyContent(UPDATED_REPLY_CONTENT)
            .status(UPDATED_STATUS)
            .memo(UPDATED_MEMO);
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(updatedReplyRecord);

        restReplyRecordMockMvc.perform(put("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isOk());

        // Validate the ReplyRecord in the database
        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeUpdate);
        ReplyRecord testReplyRecord = replyRecordList.get(replyRecordList.size() - 1);
        assertThat(testReplyRecord.getStrangerEmail()).isEqualTo(UPDATED_STRANGER_EMAIL);
        assertThat(testReplyRecord.getStrangerName()).isEqualTo(UPDATED_STRANGER_NAME);
        assertThat(testReplyRecord.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testReplyRecord.getQuestionCode()).isEqualTo(UPDATED_QUESTION_CODE);
        assertThat(testReplyRecord.getHandleTypeCode()).isEqualTo(UPDATED_HANDLE_TYPE_CODE);
        assertThat(testReplyRecord.getReplyContent()).isEqualTo(UPDATED_REPLY_CONTENT);
        assertThat(testReplyRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReplyRecord.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingReplyRecord() throws Exception {
        int databaseSizeBeforeUpdate = replyRecordRepository.findAll().size();

        // Create the ReplyRecord
        ReplyRecordDTO replyRecordDTO = replyRecordMapper.toDto(replyRecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReplyRecordMockMvc.perform(put("/api/reply-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the ReplyRecord in the database
        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReplyRecord() throws Exception {
        // Initialize the database
        replyRecordRepository.saveAndFlush(replyRecord);
        int databaseSizeBeforeDelete = replyRecordRepository.findAll().size();

        // Get the replyRecord
        restReplyRecordMockMvc.perform(delete("/api/reply-records/{id}", replyRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReplyRecord> replyRecordList = replyRecordRepository.findAll();
        assertThat(replyRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReplyRecord.class);
        ReplyRecord replyRecord1 = new ReplyRecord();
        replyRecord1.setId(1L);
        ReplyRecord replyRecord2 = new ReplyRecord();
        replyRecord2.setId(replyRecord1.getId());
        assertThat(replyRecord1).isEqualTo(replyRecord2);
        replyRecord2.setId(2L);
        assertThat(replyRecord1).isNotEqualTo(replyRecord2);
        replyRecord1.setId(null);
        assertThat(replyRecord1).isNotEqualTo(replyRecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReplyRecordDTO.class);
        ReplyRecordDTO replyRecordDTO1 = new ReplyRecordDTO();
        replyRecordDTO1.setId(1L);
        ReplyRecordDTO replyRecordDTO2 = new ReplyRecordDTO();
        assertThat(replyRecordDTO1).isNotEqualTo(replyRecordDTO2);
        replyRecordDTO2.setId(replyRecordDTO1.getId());
        assertThat(replyRecordDTO1).isEqualTo(replyRecordDTO2);
        replyRecordDTO2.setId(2L);
        assertThat(replyRecordDTO1).isNotEqualTo(replyRecordDTO2);
        replyRecordDTO1.setId(null);
        assertThat(replyRecordDTO1).isNotEqualTo(replyRecordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
//        assertThat(replyRecordMapper.fromId(42L).getId()).isEqualTo(42);
//        assertThat(replyRecordMapper.fromId(null)).isNull();
    }
}
