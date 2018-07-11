package org.fwoxford.web.rest;

import org.fwoxford.MisBbisQuestionApp;
import org.fwoxford.web.rest.util.BankUtil;
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
        String a  = BankUtil.string2Unicode("546L6ZOB5p+xfFEyMDE4MDcwNjAwMnxzdWlzaG91eHVlQDE2My5jb2185bCP5p2O");
        System.out.println("解密前："+ a);
        String dd = BankUtil.unicode2String(a);
        byte[] byteArray =  (new BASE64Decoder()).decodeBuffer(dd);
        System.out.println("解密："+ new String(byteArray));
        System.out.println("解密："+ new String(byteArray));
    }
}
