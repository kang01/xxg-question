package org.fwoxford.web.rest;

import org.fwoxford.MisBbisQuestionApp;

import org.fwoxford.domain.ReplyDetails;
import org.fwoxford.domain.ReplyRecord;
import org.fwoxford.domain.QuestionItemDetails;
import org.fwoxford.repository.ReplyDetailsRepository;
import org.fwoxford.service.ReplyDetailsService;
import org.fwoxford.service.dto.ReplyDetailsDTO;
import org.fwoxford.service.mapper.ReplyDetailsMapper;
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
 * Test class for the ReplyDetailsResource REST controller.
 *
 * @see ReplyDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisBbisQuestionApp.class)
public class ReplyDetailsResourceIntTest {

    private static final String DEFAULT_HANDLE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_HANDLE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REPLY_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_REPLY_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private ReplyDetailsRepository replyDetailsRepository;

    @Autowired
    private ReplyDetailsMapper replyDetailsMapper;

    @Autowired
    private ReplyDetailsService replyDetailsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReplyDetailsMockMvc;

    private ReplyDetails replyDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReplyDetailsResource replyDetailsResource = new ReplyDetailsResource(replyDetailsService);
        this.restReplyDetailsMockMvc = MockMvcBuilders.standaloneSetup(replyDetailsResource)
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
    public static ReplyDetails createEntity(EntityManager em) {
        ReplyDetails replyDetails = new ReplyDetails()
            .handleTypeCode(DEFAULT_HANDLE_TYPE_CODE)
            .replyContent(DEFAULT_REPLY_CONTENT)
            .status(DEFAULT_STATUS)
            .memo(DEFAULT_MEMO);
        // Add required entity
        ReplyRecord replyRecord = ReplyRecordResourceIntTest.createEntity(em);
        em.persist(replyRecord);
        em.flush();
        replyDetails.setReplyRecord(replyRecord);
        return replyDetails;
    }

    @Before
    public void initTest() {
        replyDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createReplyDetails() throws Exception {
        int databaseSizeBeforeCreate = replyDetailsRepository.findAll().size();

        // Create the ReplyDetails
        ReplyDetailsDTO replyDetailsDTO = replyDetailsMapper.toDto(replyDetails);
        restReplyDetailsMockMvc.perform(post("/api/reply-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the ReplyDetails in the database
        List<ReplyDetails> replyDetailsList = replyDetailsRepository.findAll();
        assertThat(replyDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        ReplyDetails testReplyDetails = replyDetailsList.get(replyDetailsList.size() - 1);
        assertThat(testReplyDetails.getHandleTypeCode()).isEqualTo(DEFAULT_HANDLE_TYPE_CODE);
        assertThat(testReplyDetails.getReplyContent()).isEqualTo(DEFAULT_REPLY_CONTENT);
        assertThat(testReplyDetails.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReplyDetails.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createReplyDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = replyDetailsRepository.findAll().size();

        // Create the ReplyDetails with an existing ID
        replyDetails.setId(1L);
        ReplyDetailsDTO replyDetailsDTO = replyDetailsMapper.toDto(replyDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReplyDetailsMockMvc.perform(post("/api/reply-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReplyDetails in the database
        List<ReplyDetails> replyDetailsList = replyDetailsRepository.findAll();
        assertThat(replyDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHandleTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyDetailsRepository.findAll().size();
        // set the field null
        replyDetails.setHandleTypeCode(null);

        // Create the ReplyDetails, which fails.
        ReplyDetailsDTO replyDetailsDTO = replyDetailsMapper.toDto(replyDetails);

        restReplyDetailsMockMvc.perform(post("/api/reply-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyDetails> replyDetailsList = replyDetailsRepository.findAll();
        assertThat(replyDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReplyContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyDetailsRepository.findAll().size();
        // set the field null
        replyDetails.setReplyContent(null);

        // Create the ReplyDetails, which fails.
        ReplyDetailsDTO replyDetailsDTO = replyDetailsMapper.toDto(replyDetails);

        restReplyDetailsMockMvc.perform(post("/api/reply-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyDetails> replyDetailsList = replyDetailsRepository.findAll();
        assertThat(replyDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = replyDetailsRepository.findAll().size();
        // set the field null
        replyDetails.setStatus(null);

        // Create the ReplyDetails, which fails.
        ReplyDetailsDTO replyDetailsDTO = replyDetailsMapper.toDto(replyDetails);

        restReplyDetailsMockMvc.perform(post("/api/reply-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<ReplyDetails> replyDetailsList = replyDetailsRepository.findAll();
        assertThat(replyDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReplyDetails() throws Exception {
        // Initialize the database
        replyDetailsRepository.saveAndFlush(replyDetails);

        // Get all the replyDetailsList
        restReplyDetailsMockMvc.perform(get("/api/reply-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(replyDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].handleTypeCode").value(hasItem(DEFAULT_HANDLE_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].replyContent").value(hasItem(DEFAULT_REPLY_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getReplyDetails() throws Exception {
        // Initialize the database
        replyDetailsRepository.saveAndFlush(replyDetails);

        // Get the replyDetails
        restReplyDetailsMockMvc.perform(get("/api/reply-details/{id}", replyDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(replyDetails.getId().intValue()))
            .andExpect(jsonPath("$.handleTypeCode").value(DEFAULT_HANDLE_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.replyContent").value(DEFAULT_REPLY_CONTENT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReplyDetails() throws Exception {
        // Get the replyDetails
        restReplyDetailsMockMvc.perform(get("/api/reply-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReplyDetails() throws Exception {
        // Initialize the database
        replyDetailsRepository.saveAndFlush(replyDetails);
        int databaseSizeBeforeUpdate = replyDetailsRepository.findAll().size();

        // Update the replyDetails
        ReplyDetails updatedReplyDetails = replyDetailsRepository.findOne(replyDetails.getId());
        // Disconnect from session so that the updates on updatedReplyDetails are not directly saved in db
        em.detach(updatedReplyDetails);
        updatedReplyDetails
            .handleTypeCode(UPDATED_HANDLE_TYPE_CODE)
            .replyContent(UPDATED_REPLY_CONTENT)
            .status(UPDATED_STATUS)
            .memo(UPDATED_MEMO);
        ReplyDetailsDTO replyDetailsDTO = replyDetailsMapper.toDto(updatedReplyDetails);

        restReplyDetailsMockMvc.perform(put("/api/reply-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the ReplyDetails in the database
        List<ReplyDetails> replyDetailsList = replyDetailsRepository.findAll();
        assertThat(replyDetailsList).hasSize(databaseSizeBeforeUpdate);
        ReplyDetails testReplyDetails = replyDetailsList.get(replyDetailsList.size() - 1);
        assertThat(testReplyDetails.getHandleTypeCode()).isEqualTo(UPDATED_HANDLE_TYPE_CODE);
        assertThat(testReplyDetails.getReplyContent()).isEqualTo(UPDATED_REPLY_CONTENT);
        assertThat(testReplyDetails.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReplyDetails.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingReplyDetails() throws Exception {
        int databaseSizeBeforeUpdate = replyDetailsRepository.findAll().size();

        // Create the ReplyDetails
        ReplyDetailsDTO replyDetailsDTO = replyDetailsMapper.toDto(replyDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReplyDetailsMockMvc.perform(put("/api/reply-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the ReplyDetails in the database
        List<ReplyDetails> replyDetailsList = replyDetailsRepository.findAll();
        assertThat(replyDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReplyDetails() throws Exception {
        // Initialize the database
        replyDetailsRepository.saveAndFlush(replyDetails);
        int databaseSizeBeforeDelete = replyDetailsRepository.findAll().size();

        // Get the replyDetails
        restReplyDetailsMockMvc.perform(delete("/api/reply-details/{id}", replyDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReplyDetails> replyDetailsList = replyDetailsRepository.findAll();
        assertThat(replyDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReplyDetails.class);
        ReplyDetails replyDetails1 = new ReplyDetails();
        replyDetails1.setId(1L);
        ReplyDetails replyDetails2 = new ReplyDetails();
        replyDetails2.setId(replyDetails1.getId());
        assertThat(replyDetails1).isEqualTo(replyDetails2);
        replyDetails2.setId(2L);
        assertThat(replyDetails1).isNotEqualTo(replyDetails2);
        replyDetails1.setId(null);
        assertThat(replyDetails1).isNotEqualTo(replyDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReplyDetailsDTO.class);
        ReplyDetailsDTO replyDetailsDTO1 = new ReplyDetailsDTO();
        replyDetailsDTO1.setId(1L);
        ReplyDetailsDTO replyDetailsDTO2 = new ReplyDetailsDTO();
        assertThat(replyDetailsDTO1).isNotEqualTo(replyDetailsDTO2);
        replyDetailsDTO2.setId(replyDetailsDTO1.getId());
        assertThat(replyDetailsDTO1).isEqualTo(replyDetailsDTO2);
        replyDetailsDTO2.setId(2L);
        assertThat(replyDetailsDTO1).isNotEqualTo(replyDetailsDTO2);
        replyDetailsDTO1.setId(null);
        assertThat(replyDetailsDTO1).isNotEqualTo(replyDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(replyDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(replyDetailsMapper.fromId(null)).isNull();
    }
}
