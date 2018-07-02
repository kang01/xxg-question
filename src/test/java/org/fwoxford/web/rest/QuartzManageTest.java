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
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fwoxford.web.rest.TestUtil.createFormattingConversionService;
import static org.fwoxford.web.rest.TestUtil.sameInstant;
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
public class QuartzManageTest {

    @Resource(name = "scheduler")
    private Scheduler scheduler;
    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        List<JobExecutionContext> jobContexts = scheduler.getCurrentlyExecutingJobs();
        for(JobExecutionContext jobExecutionContext :jobContexts){
            String name = jobExecutionContext.getTrigger().getJobKey().getName();
            System.out.print("正在执行任务"+name);
        }
    }
}
