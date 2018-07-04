package org.fwoxford.web.rest;


import com.sun.xml.internal.messaging.saaj.util.Base64;
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
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
    @Test
    public  void main() throws Exception
    {
        String data = (new BASE64Encoder()).encode("Q0180704001|luying.geng@fwoxford.org==".getBytes()).replace("=","");
//        String base64Token = new String(Base64.encodeBase64("http://aub.iteye.com/==".getBytes("UTF-8")),"UTF-8");
        System.out.println("加密："+data);

        byte[] byteArray =  (new BASE64Decoder()).decodeBuffer(data);
        System.out.println("解密："+ new String(byteArray));
        String a = Base64Utils.encodeToUrlSafeString(data.getBytes());
        String  b=  Base64Utils.encodeToString(data.getBytes());
    }
}
