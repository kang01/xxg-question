package org.fwoxford.web.rest;

import org.fwoxford.MisBbisQuestionApp;

import org.fwoxford.domain.QuartzTask;
import org.fwoxford.repository.QuartzTaskRepository;
import org.fwoxford.service.QuartzTaskService;
import org.fwoxford.service.dto.QuartzTaskDTO;
import org.fwoxford.service.mapper.QuartzTaskMapper;
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
 * Test class for the QuartsTaskResource REST controller.
 *
 * @see QuartzTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisBbisQuestionApp.class)
public class QuartzTaskResourceIntTest {

    private static final String DEFAULT_JOB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TRIGGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENABLE_STATUS = 10;
    private static final Integer UPDATED_ENABLE_STATUS = 9;

    private static final String DEFAULT_TRIGGER_TIME = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGER_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private QuartzTaskRepository quartzTaskRepository;

    @Autowired
    private QuartzTaskMapper quartzTaskMapper;

    @Autowired
    private QuartzTaskService quartzTaskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuartsTaskMockMvc;

    private QuartzTask quartzTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuartzTaskResource quartzTaskResource = new QuartzTaskResource(quartzTaskService);
        this.restQuartsTaskMockMvc = MockMvcBuilders.standaloneSetup(quartzTaskResource)
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
    public static QuartzTask createEntity(EntityManager em) {
        QuartzTask quartzTask = new QuartzTask()
            .jobName(DEFAULT_JOB_NAME)
            .triggerName(DEFAULT_TRIGGER_NAME)
            .className(DEFAULT_CLASS_NAME)
            .enableStatus(DEFAULT_ENABLE_STATUS)
            .triggerTime(DEFAULT_TRIGGER_TIME)
            .status(DEFAULT_STATUS)
            .memo(DEFAULT_MEMO);
        return quartzTask;
    }

    @Before
    public void initTest() {
        quartzTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuartsTask() throws Exception {
        int databaseSizeBeforeCreate = quartzTaskRepository.findAll().size();

        // Create the QuartzTask
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);
        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the QuartzTask in the database
        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeCreate + 1);
        QuartzTask testQuartzTask = quartzTaskList.get(quartzTaskList.size() - 1);
        assertThat(testQuartzTask.getJobName()).isEqualTo(DEFAULT_JOB_NAME);
        assertThat(testQuartzTask.getTriggerName()).isEqualTo(DEFAULT_TRIGGER_NAME);
        assertThat(testQuartzTask.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testQuartzTask.getEnableStatus()).isEqualTo(DEFAULT_ENABLE_STATUS);
        assertThat(testQuartzTask.getTriggerTime()).isEqualTo(DEFAULT_TRIGGER_TIME);
        assertThat(testQuartzTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuartzTask.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createQuartsTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quartzTaskRepository.findAll().size();

        // Create the QuartzTask with an existing ID
        quartzTask.setId(1L);
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuartzTask in the database
        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkJobNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartzTaskRepository.findAll().size();
        // set the field null
        quartzTask.setJobName(null);

        // Create the QuartzTask, which fails.
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTriggerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartzTaskRepository.findAll().size();
        // set the field null
        quartzTask.setTriggerName(null);

        // Create the QuartzTask, which fails.
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClassNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartzTaskRepository.findAll().size();
        // set the field null
        quartzTask.setClassName(null);

        // Create the QuartzTask, which fails.
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnableStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartzTaskRepository.findAll().size();
        // set the field null
        quartzTask.setEnableStatus(null);

        // Create the QuartzTask, which fails.
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTriggerTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartzTaskRepository.findAll().size();
        // set the field null
        quartzTask.setTriggerTime(null);

        // Create the QuartzTask, which fails.
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = quartzTaskRepository.findAll().size();
        // set the field null
        quartzTask.setStatus(null);

        // Create the QuartzTask, which fails.
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);

        restQuartsTaskMockMvc.perform(post("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isBadRequest());

        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuartsTasks() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList
        restQuartsTaskMockMvc.perform(get("/api/quarts-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quartzTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
            .andExpect(jsonPath("$.[*].triggerName").value(hasItem(DEFAULT_TRIGGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].enableStatus").value(hasItem(DEFAULT_ENABLE_STATUS)))
            .andExpect(jsonPath("$.[*].triggerTime").value(hasItem((DEFAULT_TRIGGER_TIME))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getQuartsTask() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get the quartzTask
        restQuartsTaskMockMvc.perform(get("/api/quarts-tasks/{id}", quartzTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quartzTask.getId().intValue()))
            .andExpect(jsonPath("$.jobName").value(DEFAULT_JOB_NAME.toString()))
            .andExpect(jsonPath("$.triggerName").value(DEFAULT_TRIGGER_NAME.toString()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME.toString()))
            .andExpect(jsonPath("$.enableStatus").value(DEFAULT_ENABLE_STATUS))
            .andExpect(jsonPath("$.triggerTime").value((DEFAULT_TRIGGER_TIME)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByJobNameIsEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where jobName equals to DEFAULT_JOB_NAME
        defaultQuartsTaskShouldBeFound("jobName.equals=" + DEFAULT_JOB_NAME);

        // Get all the quartsTaskList where jobName equals to UPDATED_JOB_NAME
        defaultQuartsTaskShouldNotBeFound("jobName.equals=" + UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByJobNameIsInShouldWork() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where jobName in DEFAULT_JOB_NAME or UPDATED_JOB_NAME
        defaultQuartsTaskShouldBeFound("jobName.in=" + DEFAULT_JOB_NAME + "," + UPDATED_JOB_NAME);

        // Get all the quartsTaskList where jobName equals to UPDATED_JOB_NAME
        defaultQuartsTaskShouldNotBeFound("jobName.in=" + UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByJobNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where jobName is not null
        defaultQuartsTaskShouldBeFound("jobName.specified=true");

        // Get all the quartsTaskList where jobName is null
        defaultQuartsTaskShouldNotBeFound("jobName.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where triggerName equals to DEFAULT_TRIGGER_NAME
        defaultQuartsTaskShouldBeFound("triggerName.equals=" + DEFAULT_TRIGGER_NAME);

        // Get all the quartsTaskList where triggerName equals to UPDATED_TRIGGER_NAME
        defaultQuartsTaskShouldNotBeFound("triggerName.equals=" + UPDATED_TRIGGER_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerNameIsInShouldWork() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where triggerName in DEFAULT_TRIGGER_NAME or UPDATED_TRIGGER_NAME
        defaultQuartsTaskShouldBeFound("triggerName.in=" + DEFAULT_TRIGGER_NAME + "," + UPDATED_TRIGGER_NAME);

        // Get all the quartsTaskList where triggerName equals to UPDATED_TRIGGER_NAME
        defaultQuartsTaskShouldNotBeFound("triggerName.in=" + UPDATED_TRIGGER_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where triggerName is not null
        defaultQuartsTaskShouldBeFound("triggerName.specified=true");

        // Get all the quartsTaskList where triggerName is null
        defaultQuartsTaskShouldNotBeFound("triggerName.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByClassNameIsEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where className equals to DEFAULT_CLASS_NAME
        defaultQuartsTaskShouldBeFound("className.equals=" + DEFAULT_CLASS_NAME);

        // Get all the quartsTaskList where className equals to UPDATED_CLASS_NAME
        defaultQuartsTaskShouldNotBeFound("className.equals=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByClassNameIsInShouldWork() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where className in DEFAULT_CLASS_NAME or UPDATED_CLASS_NAME
        defaultQuartsTaskShouldBeFound("className.in=" + DEFAULT_CLASS_NAME + "," + UPDATED_CLASS_NAME);

        // Get all the quartsTaskList where className equals to UPDATED_CLASS_NAME
        defaultQuartsTaskShouldNotBeFound("className.in=" + UPDATED_CLASS_NAME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByClassNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where className is not null
        defaultQuartsTaskShouldBeFound("className.specified=true");

        // Get all the quartsTaskList where className is null
        defaultQuartsTaskShouldNotBeFound("className.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where enableStatus equals to DEFAULT_ENABLE_STATUS
        defaultQuartsTaskShouldBeFound("enableStatus.equals=" + DEFAULT_ENABLE_STATUS);

        // Get all the quartsTaskList where enableStatus equals to UPDATED_ENABLE_STATUS
        defaultQuartsTaskShouldNotBeFound("enableStatus.equals=" + UPDATED_ENABLE_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsInShouldWork() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where enableStatus in DEFAULT_ENABLE_STATUS or UPDATED_ENABLE_STATUS
        defaultQuartsTaskShouldBeFound("enableStatus.in=" + DEFAULT_ENABLE_STATUS + "," + UPDATED_ENABLE_STATUS);

        // Get all the quartsTaskList where enableStatus equals to UPDATED_ENABLE_STATUS
        defaultQuartsTaskShouldNotBeFound("enableStatus.in=" + UPDATED_ENABLE_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where enableStatus is not null
        defaultQuartsTaskShouldBeFound("enableStatus.specified=true");

        // Get all the quartsTaskList where enableStatus is null
        defaultQuartsTaskShouldNotBeFound("enableStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where enableStatus greater than or equals to DEFAULT_ENABLE_STATUS
        defaultQuartsTaskShouldBeFound("enableStatus.greaterOrEqualThan=" + DEFAULT_ENABLE_STATUS);

        // Get all the quartsTaskList where enableStatus greater than or equals to (DEFAULT_ENABLE_STATUS + 1)
        defaultQuartsTaskShouldNotBeFound("enableStatus.greaterOrEqualThan=" + (DEFAULT_ENABLE_STATUS + 1));
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByEnableStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where enableStatus less than or equals to DEFAULT_ENABLE_STATUS
        defaultQuartsTaskShouldNotBeFound("enableStatus.lessThan=" + DEFAULT_ENABLE_STATUS);

        // Get all the quartsTaskList where enableStatus less than or equals to (DEFAULT_ENABLE_STATUS + 1)
        defaultQuartsTaskShouldBeFound("enableStatus.lessThan=" + (DEFAULT_ENABLE_STATUS + 1));
    }


    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where triggerTime equals to DEFAULT_TRIGGER_TIME
        defaultQuartsTaskShouldBeFound("triggerTime.equals=" + DEFAULT_TRIGGER_TIME);

        // Get all the quartsTaskList where triggerTime equals to UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldNotBeFound("triggerTime.equals=" + UPDATED_TRIGGER_TIME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsInShouldWork() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where triggerTime in DEFAULT_TRIGGER_TIME or UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldBeFound("triggerTime.in=" + DEFAULT_TRIGGER_TIME + "," + UPDATED_TRIGGER_TIME);

        // Get all the quartsTaskList where triggerTime equals to UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldNotBeFound("triggerTime.in=" + UPDATED_TRIGGER_TIME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where triggerTime is not null
        defaultQuartsTaskShouldBeFound("triggerTime.specified=true");

        // Get all the quartsTaskList where triggerTime is null
        defaultQuartsTaskShouldNotBeFound("triggerTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where triggerTime greater than or equals to DEFAULT_TRIGGER_TIME
        defaultQuartsTaskShouldBeFound("triggerTime.greaterOrEqualThan=" + DEFAULT_TRIGGER_TIME);

        // Get all the quartsTaskList where triggerTime greater than or equals to UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldNotBeFound("triggerTime.greaterOrEqualThan=" + UPDATED_TRIGGER_TIME);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByTriggerTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where triggerTime less than or equals to DEFAULT_TRIGGER_TIME
        defaultQuartsTaskShouldNotBeFound("triggerTime.lessThan=" + DEFAULT_TRIGGER_TIME);

        // Get all the quartsTaskList where triggerTime less than or equals to UPDATED_TRIGGER_TIME
        defaultQuartsTaskShouldBeFound("triggerTime.lessThan=" + UPDATED_TRIGGER_TIME);
    }


    @Test
    @Transactional
    public void getAllQuartsTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where status equals to DEFAULT_STATUS
        defaultQuartsTaskShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the quartsTaskList where status equals to UPDATED_STATUS
        defaultQuartsTaskShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultQuartsTaskShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the quartsTaskList where status equals to UPDATED_STATUS
        defaultQuartsTaskShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where status is not null
        defaultQuartsTaskShouldBeFound("status.specified=true");

        // Get all the quartsTaskList where status is null
        defaultQuartsTaskShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByMemoIsEqualToSomething() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where memo equals to DEFAULT_MEMO
        defaultQuartsTaskShouldBeFound("memo.equals=" + DEFAULT_MEMO);

        // Get all the quartsTaskList where memo equals to UPDATED_MEMO
        defaultQuartsTaskShouldNotBeFound("memo.equals=" + UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByMemoIsInShouldWork() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

        // Get all the quartsTaskList where memo in DEFAULT_MEMO or UPDATED_MEMO
        defaultQuartsTaskShouldBeFound("memo.in=" + DEFAULT_MEMO + "," + UPDATED_MEMO);

        // Get all the quartsTaskList where memo equals to UPDATED_MEMO
        defaultQuartsTaskShouldNotBeFound("memo.in=" + UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void getAllQuartsTasksByMemoIsNullOrNotNull() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);

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
            .andExpect(jsonPath("$.[*].id").value(hasItem(quartzTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
            .andExpect(jsonPath("$.[*].triggerName").value(hasItem(DEFAULT_TRIGGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].enableStatus").value(hasItem(DEFAULT_ENABLE_STATUS)))
            .andExpect(jsonPath("$.[*].triggerTime").value(hasItem((DEFAULT_TRIGGER_TIME))))
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
        // Get the quartzTask
        restQuartsTaskMockMvc.perform(get("/api/quarts-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuartsTask() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);
        int databaseSizeBeforeUpdate = quartzTaskRepository.findAll().size();

        // Update the quartzTask
        QuartzTask updatedQuartzTask = quartzTaskRepository.findOne(quartzTask.getId());
        // Disconnect from session so that the updates on updatedQuartzTask are not directly saved in db
        em.detach(updatedQuartzTask);
        updatedQuartzTask
            .jobName(UPDATED_JOB_NAME)
            .triggerName(UPDATED_TRIGGER_NAME)
            .className(UPDATED_CLASS_NAME)
            .enableStatus(UPDATED_ENABLE_STATUS)
            .triggerTime(UPDATED_TRIGGER_TIME)
            .status(UPDATED_STATUS)
            .memo(UPDATED_MEMO);
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(updatedQuartzTask);

        restQuartsTaskMockMvc.perform(put("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isOk());

        // Validate the QuartzTask in the database
        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeUpdate);
        QuartzTask testQuartzTask = quartzTaskList.get(quartzTaskList.size() - 1);
        assertThat(testQuartzTask.getJobName()).isEqualTo(UPDATED_JOB_NAME);
        assertThat(testQuartzTask.getTriggerName()).isEqualTo(UPDATED_TRIGGER_NAME);
        assertThat(testQuartzTask.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testQuartzTask.getEnableStatus()).isEqualTo(UPDATED_ENABLE_STATUS);
        assertThat(testQuartzTask.getTriggerTime()).isEqualTo(UPDATED_TRIGGER_TIME);
        assertThat(testQuartzTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuartzTask.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingQuartsTask() throws Exception {
        int databaseSizeBeforeUpdate = quartzTaskRepository.findAll().size();

        // Create the QuartzTask
        QuartzTaskDTO quartzTaskDTO = quartzTaskMapper.toDto(quartzTask);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuartsTaskMockMvc.perform(put("/api/quarts-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quartzTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the QuartzTask in the database
        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuartsTask() throws Exception {
        // Initialize the database
        quartzTaskRepository.saveAndFlush(quartzTask);
        int databaseSizeBeforeDelete = quartzTaskRepository.findAll().size();

        // Get the quartzTask
        restQuartsTaskMockMvc.perform(delete("/api/quarts-tasks/{id}", quartzTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuartzTask> quartzTaskList = quartzTaskRepository.findAll();
        assertThat(quartzTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuartzTask.class);
        QuartzTask quartzTask1 = new QuartzTask();
        quartzTask1.setId(1L);
        QuartzTask quartzTask2 = new QuartzTask();
        quartzTask2.setId(quartzTask1.getId());
        assertThat(quartzTask1).isEqualTo(quartzTask2);
        quartzTask2.setId(2L);
        assertThat(quartzTask1).isNotEqualTo(quartzTask2);
        quartzTask1.setId(null);
        assertThat(quartzTask1).isNotEqualTo(quartzTask2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuartzTaskDTO.class);
        QuartzTaskDTO quartzTaskDTO1 = new QuartzTaskDTO();
        quartzTaskDTO1.setId(1L);
        QuartzTaskDTO quartzTaskDTO2 = new QuartzTaskDTO();
        assertThat(quartzTaskDTO1).isNotEqualTo(quartzTaskDTO2);
        quartzTaskDTO2.setId(quartzTaskDTO1.getId());
        assertThat(quartzTaskDTO1).isEqualTo(quartzTaskDTO2);
        quartzTaskDTO2.setId(2L);
        assertThat(quartzTaskDTO1).isNotEqualTo(quartzTaskDTO2);
        quartzTaskDTO1.setId(null);
        assertThat(quartzTaskDTO1).isNotEqualTo(quartzTaskDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(quartzTaskMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(quartzTaskMapper.fromId(null)).isNull();
    }
}
