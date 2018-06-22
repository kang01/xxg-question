package org.fwoxford.web.rest;

import org.fwoxford.MisBbisQuestionApp;

import org.fwoxford.domain.QuartsTask;
import org.fwoxford.repository.QuartsTaskRepository;
import org.fwoxford.service.QuartsTaskService;
import org.fwoxford.service.dto.QuartsTaskDTO;
import org.fwoxford.service.mapper.QuartsTaskMapper;
import org.fwoxford.web.rest.errors.ExceptionTranslator;
import org.fwoxford.service.dto.QuartsTaskCriteria;
import org.fwoxford.service.QuartsTaskQueryService;

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
 * Test class for the QuartsTaskResource REST controller.
 *
 * @see QuartsTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisBbisQuestionApp.class)
public class QuartsTaskResourceIntTest {

    private static final String DEFAULT_JOB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TRIGGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENABLE_STATUS = 10;
    private static final Integer UPDATED_ENABLE_STATUS = 9;

    private static final ZonedDateTime DEFAULT_TRIGGER_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRIGGER_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private QuartsTaskRepository quartsTaskRepository;

    @Autowired
    private QuartsTaskMapper quartsTaskMapper;

    @Autowired
    private QuartsTaskService quartsTaskService;

    @Autowired
    private QuartsTaskQueryService quartsTaskQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuartsTaskMockMvc;

    private QuartsTask quartsTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuartsTaskResource quartsTaskResource = new QuartsTaskResource(quartsTaskService, quartsTaskQueryService);
        this.restQuartsTaskMockMvc = MockMvcBuilders.standaloneSetup(quartsTaskResource)
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
    public static QuartsTask createEntity(EntityManager em) {
        QuartsTask quartsTask = new QuartsTask()
            .jobName(DEFAULT_JOB_NAME)
            .triggerName(DEFAULT_TRIGGER_NAME)
            .className(DEFAULT_CLASS_NAME)
            .enableStatus(DEFAULT_ENABLE_STATUS)
            .triggerTime(DEFAULT_TRIGGER_TIME)
            .status(DEFAULT_STATUS)
            .memo(DEFAULT_MEMO);
        return quartsTask;
    }

    @Before
    public void initTest() {
        quartsTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuartsTask() throws Exception {
        int databaseSizeBeforeCreate = quartsTaskRepository.findAll().size();

        // Create the QuartsTask
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);
        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the QuartsTask in the database
        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeCreate + 1);
        QuartsTask testQuartsTask = quartsTaskList.get(quartsTaskList.size() - 1);
        assertThat(testQuartsTask.getJobName()).isEqualTo(DEFAULT_JOB_NAME);
        assertThat(testQuartsTask.getTriggerName()).isEqualTo(DEFAULT_TRIGGER_NAME);
        assertThat(testQuartsTask.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testQuartsTask.getEnableStatus()).isEqualTo(DEFAULT_ENABLE_STATUS);
        assertThat(testQuartsTask.getTriggerTime()).isEqualTo(DEFAULT_TRIGGER_TIME);
        assertThat(testQuartsTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuartsTask.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createQuartsTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quartsTaskRepository.findAll().size();

        // Create the QuartsTask with an existing ID
        quartsTask.setId(1L);
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuartsTask in the database
        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkJobNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartsTaskRepository.findAll().size();
        // set the field null
        quartsTask.setJobName(null);

        // Create the QuartsTask, which fails.
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTriggerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartsTaskRepository.findAll().size();
        // set the field null
        quartsTask.setTriggerName(null);

        // Create the QuartsTask, which fails.
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClassNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartsTaskRepository.findAll().size();
        // set the field null
        quartsTask.setClassName(null);

        // Create the QuartsTask, which fails.
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnableStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartsTaskRepository.findAll().size();
        // set the field null
        quartsTask.setEnableStatus(null);

        // Create the QuartsTask, which fails.
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTriggerTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartsTaskRepository.findAll().size();
        // set the field null
        quartsTask.setTriggerTime(null);

        // Create the QuartsTask, which fails.
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartsTaskRepository.findAll().size();
        // set the field null
        quartsTask.setStatus(null);

        // Create the QuartsTask, which fails.
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuartsTasks() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList
        restQuartsTaskMockMvc.perform(get("/api/quarts-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quartsTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
            .andExpect(jsonPath("$.[*].triggerName").value(hasItem(DEFAULT_TRIGGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].enableStatus").value(hasItem(DEFAULT_ENABLE_STATUS)))
            .andExpect(jsonPath("$.[*].triggerTime").value(hasItem(sameInstant(DEFAULT_TRIGGER_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getQuartsTask() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get the quartsTask
        restQuartsTaskMockMvc.perform(get("/api/quarts-tasks/{id}", quartsTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quartsTask.getId().intValue()))
            .andExpect(jsonPath("$.jobName").value(DEFAULT_JOB_NAME.toString()))
            .andExpect(jsonPath("$.triggerName").value(DEFAULT_TRIGGER_NAME.toString()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME.toString()))
            .andExpect(jsonPath("$.enableStatus").value(DEFAULT_ENABLE_STATUS))
            .andExpect(jsonPath("$.triggerTime").value(sameInstant(DEFAULT_TRIGGER_TIME)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByJobNameIsEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where jobName equals to DEFAULT_JOB_NAME
        defaultQuartsTaskShouldBeFound("jobName.equals=" + DEFAULT_JOB_NAME);

        // Get all the quartsTaskList where jobName equals to UPDATED_JOB_NAME
        defaultQuartsTaskShouldNotBeFound("jobName.equals=" + UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByJobNameIsInShouldWork() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where jobName in DEFAULT_JOB_NAME or UPDATED_JOB_NAME
        defaultQuartsTaskShouldBeFound("jobName.in=" + DEFAULT_JOB_NAME + "," + UPDATED_JOB_NAME);

        // Get all the quartsTaskList where jobName equals to UPDATED_JOB_NAME
        defaultQuartsTaskShouldNotBeFound("jobName.in=" + UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByJobNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where jobName is not null
        defaultQuartsTaskShouldBeFound("jobName.specified=true");

        // Get all the quartsTaskList where jobName is null
        defaultQuartsTaskShouldNotBeFound("jobName.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where triggerName equals to DEFAULT_TRIGGER_NAME
        defaultQuartsTaskShouldBeFound("triggerName.equals=" + DEFAULT_TRIGGER_NAME);

        // Get all the quartsTaskList where triggerName equals to UPDATED_TRIGGER_NAME
        defaultQuartsTaskShouldNotBeFound("triggerName.equals=" + UPDATED_TRIGGER_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerNameIsInShouldWork() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where triggerName in DEFAULT_TRIGGER_NAME or UPDATED_TRIGGER_NAME
        defaultQuartsTaskShouldBeFound("triggerName.in=" + DEFAULT_TRIGGER_NAME + "," + UPDATED_TRIGGER_NAME);

        // Get all the quartsTaskList where triggerName equals to UPDATED_TRIGGER_NAME
        defaultQuartsTaskShouldNotBeFound("triggerName.in=" + UPDATED_TRIGGER_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where triggerName is not null
        defaultQuartsTaskShouldBeFound("triggerName.specified=true");

        // Get all the quartsTaskList where triggerName is null
        defaultQuartsTaskShouldNotBeFound("triggerName.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByClassNameIsEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where className equals to DEFAULT_CLASS_NAME
        defaultQuartsTaskShouldBeFound("className.equals=" + DEFAULT_CLASS_NAME);

        // Get all the quartsTaskList where className equals to UPDATED_CLASS_NAME
        defaultQuartsTaskShouldNotBeFound("className.equals=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByClassNameIsInShouldWork() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where className in DEFAULT_CLASS_NAME or UPDATED_CLASS_NAME
        defaultQuartsTaskShouldBeFound("className.in=" + DEFAULT_CLASS_NAME + "," + UPDATED_CLASS_NAME);

        // Get all the quartsTaskList where className equals to UPDATED_CLASS_NAME
        defaultQuartsTaskShouldNotBeFound("className.in=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByClassNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where className is not null
        defaultQuartsTaskShouldBeFound("className.specified=true");

        // Get all the quartsTaskList where className is null
        defaultQuartsTaskShouldNotBeFound("className.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where enableStatus equals to DEFAULT_ENABLE_STATUS
        defaultQuartsTaskShouldBeFound("enableStatus.equals=" + DEFAULT_ENABLE_STATUS);

        // Get all the quartsTaskList where enableStatus equals to UPDATED_ENABLE_STATUS
        defaultQuartsTaskShouldNotBeFound("enableStatus.equals=" + UPDATED_ENABLE_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsInShouldWork() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where enableStatus in DEFAULT_ENABLE_STATUS or UPDATED_ENABLE_STATUS
        defaultQuartsTaskShouldBeFound("enableStatus.in=" + DEFAULT_ENABLE_STATUS + "," + UPDATED_ENABLE_STATUS);

        // Get all the quartsTaskList where enableStatus equals to UPDATED_ENABLE_STATUS
        defaultQuartsTaskShouldNotBeFound("enableStatus.in=" + UPDATED_ENABLE_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where enableStatus is not null
        defaultQuartsTaskShouldBeFound("enableStatus.specified=true");

        // Get all the quartsTaskList where enableStatus is null
        defaultQuartsTaskShouldNotBeFound("enableStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where enableStatus greater than or equals to DEFAULT_ENABLE_STATUS
        defaultQuartsTaskShouldBeFound("enableStatus.greaterOrEqualThan=" + DEFAULT_ENABLE_STATUS);

        // Get all the quartsTaskList where enableStatus greater than or equals to (DEFAULT_ENABLE_STATUS + 1)
        defaultQuartsTaskShouldNotBeFound("enableStatus.greaterOrEqualThan=" + (DEFAULT_ENABLE_STATUS + 1));
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where enableStatus less than or equals to DEFAULT_ENABLE_STATUS
        defaultQuartsTaskShouldNotBeFound("enableStatus.lessThan=" + DEFAULT_ENABLE_STATUS);

        // Get all the quartsTaskList where enableStatus less than or equals to (DEFAULT_ENABLE_STATUS + 1)
        defaultQuartsTaskShouldBeFound("enableStatus.lessThan=" + (DEFAULT_ENABLE_STATUS + 1));
    }


    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where triggerTime equals to DEFAULT_TRIGGER_TIME
        defaultQuartsTaskShouldBeFound("triggerTime.equals=" + DEFAULT_TRIGGER_TIME);

        // Get all the quartsTaskList where triggerTime equals to UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldNotBeFound("triggerTime.equals=" + UPDATED_TRIGGER_TIME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsInShouldWork() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where triggerTime in DEFAULT_TRIGGER_TIME or UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldBeFound("triggerTime.in=" + DEFAULT_TRIGGER_TIME + "," + UPDATED_TRIGGER_TIME);

        // Get all the quartsTaskList where triggerTime equals to UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldNotBeFound("triggerTime.in=" + UPDATED_TRIGGER_TIME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where triggerTime is not null
        defaultQuartsTaskShouldBeFound("triggerTime.specified=true");

        // Get all the quartsTaskList where triggerTime is null
        defaultQuartsTaskShouldNotBeFound("triggerTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where triggerTime greater than or equals to DEFAULT_TRIGGER_TIME
        defaultQuartsTaskShouldBeFound("triggerTime.greaterOrEqualThan=" + DEFAULT_TRIGGER_TIME);

        // Get all the quartsTaskList where triggerTime greater than or equals to UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldNotBeFound("triggerTime.greaterOrEqualThan=" + UPDATED_TRIGGER_TIME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where triggerTime less than or equals to DEFAULT_TRIGGER_TIME
        defaultQuartsTaskShouldNotBeFound("triggerTime.lessThan=" + DEFAULT_TRIGGER_TIME);

        // Get all the quartsTaskList where triggerTime less than or equals to UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldBeFound("triggerTime.lessThan=" + UPDATED_TRIGGER_TIME);
    }


    @Test
    @Transactional
    public void getAllQuartsTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where status equals to DEFAULT_STATUS
        defaultQuartsTaskShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the quartsTaskList where status equals to UPDATED_STATUS
        defaultQuartsTaskShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultQuartsTaskShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the quartsTaskList where status equals to UPDATED_STATUS
        defaultQuartsTaskShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where status is not null
        defaultQuartsTaskShouldBeFound("status.specified=true");

        // Get all the quartsTaskList where status is null
        defaultQuartsTaskShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByMemoIsEqualToSomething() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where memo equals to DEFAULT_MEMO
        defaultQuartsTaskShouldBeFound("memo.equals=" + DEFAULT_MEMO);

        // Get all the quartsTaskList where memo equals to UPDATED_MEMO
        defaultQuartsTaskShouldNotBeFound("memo.equals=" + UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByMemoIsInShouldWork() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where memo in DEFAULT_MEMO or UPDATED_MEMO
        defaultQuartsTaskShouldBeFound("memo.in=" + DEFAULT_MEMO + "," + UPDATED_MEMO);

        // Get all the quartsTaskList where memo equals to UPDATED_MEMO
        defaultQuartsTaskShouldNotBeFound("memo.in=" + UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByMemoIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);

        // Get all the quartsTaskList where memo is not null
        defaultQuartsTaskShouldBeFound("memo.specified=true");

        // Get all the quartsTaskList where memo is null
        defaultQuartsTaskShouldNotBeFound("memo.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuartsTaskShouldBeFound(String filter) throws Exception {
        restQuartsTaskMockMvc.perform(get("/api/quarts-tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quartsTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
            .andExpect(jsonPath("$.[*].triggerName").value(hasItem(DEFAULT_TRIGGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].enableStatus").value(hasItem(DEFAULT_ENABLE_STATUS)))
            .andExpect(jsonPath("$.[*].triggerTime").value(hasItem(sameInstant(DEFAULT_TRIGGER_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuartsTaskShouldNotBeFound(String filter) throws Exception {
        restQuartsTaskMockMvc.perform(get("/api/quarts-tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingQuartsTask() throws Exception {
        // Get the quartsTask
        restQuartsTaskMockMvc.perform(get("/api/quarts-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuartsTask() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);
        int databaseSizeBeforeUpdate = quartsTaskRepository.findAll().size();

        // Update the quartsTask
        QuartsTask updatedQuartsTask = quartsTaskRepository.findOne(quartsTask.getId());
        // Disconnect from session so that the updates on updatedQuartsTask are not directly saved in db
        em.detach(updatedQuartsTask);
        updatedQuartsTask
            .jobName(UPDATED_JOB_NAME)
            .triggerName(UPDATED_TRIGGER_NAME)
            .className(UPDATED_CLASS_NAME)
            .enableStatus(UPDATED_ENABLE_STATUS)
            .triggerTime(UPDATED_TRIGGER_TIME)
            .status(UPDATED_STATUS)
            .memo(UPDATED_MEMO);
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(updatedQuartsTask);

        restQuartsTaskMockMvc.perform(put("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isOk());

        // Validate the QuartsTask in the database
        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeUpdate);
        QuartsTask testQuartsTask = quartsTaskList.get(quartsTaskList.size() - 1);
        assertThat(testQuartsTask.getJobName()).isEqualTo(UPDATED_JOB_NAME);
        assertThat(testQuartsTask.getTriggerName()).isEqualTo(UPDATED_TRIGGER_NAME);
        assertThat(testQuartsTask.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testQuartsTask.getEnableStatus()).isEqualTo(UPDATED_ENABLE_STATUS);
        assertThat(testQuartsTask.getTriggerTime()).isEqualTo(UPDATED_TRIGGER_TIME);
        assertThat(testQuartsTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuartsTask.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingQuartsTask() throws Exception {
        int databaseSizeBeforeUpdate = quartsTaskRepository.findAll().size();

        // Create the QuartsTask
        QuartsTaskDTO quartsTaskDTO = quartsTaskMapper.toDto(quartsTask);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuartsTaskMockMvc.perform(put("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartsTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the QuartsTask in the database
        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuartsTask() throws Exception {
        // Initialize the database
        quartsTaskRepository.saveAndFlush(quartsTask);
        int databaseSizeBeforeDelete = quartsTaskRepository.findAll().size();

        // Get the quartsTask
        restQuartsTaskMockMvc.perform(delete("/api/quarts-tasks/{id}", quartsTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuartsTask> quartsTaskList = quartsTaskRepository.findAll();
        assertThat(quartsTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuartsTask.class);
        QuartsTask quartsTask1 = new QuartsTask();
        quartsTask1.setId(1L);
        QuartsTask quartsTask2 = new QuartsTask();
        quartsTask2.setId(quartsTask1.getId());
        assertThat(quartsTask1).isEqualTo(quartsTask2);
        quartsTask2.setId(2L);
        assertThat(quartsTask1).isNotEqualTo(quartsTask2);
        quartsTask1.setId(null);
        assertThat(quartsTask1).isNotEqualTo(quartsTask2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuartsTaskDTO.class);
        QuartsTaskDTO quartsTaskDTO1 = new QuartsTaskDTO();
        quartsTaskDTO1.setId(1L);
        QuartsTaskDTO quartsTaskDTO2 = new QuartsTaskDTO();
        assertThat(quartsTaskDTO1).isNotEqualTo(quartsTaskDTO2);
        quartsTaskDTO2.setId(quartsTaskDTO1.getId());
        assertThat(quartsTaskDTO1).isEqualTo(quartsTaskDTO2);
        quartsTaskDTO2.setId(2L);
        assertThat(quartsTaskDTO1).isNotEqualTo(quartsTaskDTO2);
        quartsTaskDTO1.setId(null);
        assertThat(quartsTaskDTO1).isNotEqualTo(quartsTaskDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(quartsTaskMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(quartsTaskMapper.fromId(null)).isNull();
    }
}
