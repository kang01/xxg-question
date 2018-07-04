package org.fwoxford.web.rest;

import org.fwoxford.MisBbisQuestionApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
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
