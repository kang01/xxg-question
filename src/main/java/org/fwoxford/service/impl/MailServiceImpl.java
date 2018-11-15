package org.fwoxford.service.impl;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.fwoxford.service.MailService;
import org.fwoxford.service.dto.MessagerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by gengluying on 2018/6/14.
 */
@Service
@Transactional
public class MailServiceImpl implements MailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);
    //注入MailSender
    @Autowired
    JavaMailSender mailSender;

    //发送邮件的模板引擎
    @Autowired
    FreeMarkerConfigurer configurer;


    /**
     * @param params       发送邮件的主题对象 object
     * @param title        邮件标题
     * @param templateName 模板名称
     * @param messagerDTO
     */
    @Override
    public void sendMessageMail(Object params, String title, String templateName, MessagerDTO messagerDTO) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            String nick="";
            try {
                nick=javax.mail.internet.MimeUtility.encodeText("样本库信息管理系统");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            helper.setFrom(new InternetAddress(nick+" <"+messagerDTO.getFromUser()+">"));
            helper.setTo(InternetAddress.parse(messagerDTO.getToUser()));//发送给谁
            if (StringUtils.isEmpty(title)) {
                title = "问题样本确认";
            }
            helper.setSubject(title);
            Map<String, Object> model = new HashMap<>();
            model.put("params", params);
            Template template = configurer.getConfiguration().getTemplate(templateName);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(text, true);
            mailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", messagerDTO.getToUser());
        } catch (MessagingException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
