package org.fwoxford.web.rest;

import io.github.jhipster.config.JHipsterProperties;
import org.fwoxford.MisBbisQuestionApp;
import org.fwoxford.service.MailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ProfileInfoResource REST controller.
 *
 * @see ProfileInfoResource
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisBbisQuestionApp.class)
public class MailServiceIntTest {

    @Autowired
    MailService mailService;
    @Test
    public void test() throws Exception {

        Map<String,String> map = new HashMap<>();
        map.put("messageCode","MissingParameter");
        map.put("messageStatus","Failed");
        map.put("cause","缺少参数,请确认");

        mailService.sendMessageMail(map, "测试消息通知", "message.ftl");
    }

}
