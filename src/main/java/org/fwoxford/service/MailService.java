package org.fwoxford.service;

/**
 * Created by gengluying on 2018/6/14.
 */
public interface MailService {

    void sendMessageMail(Object params, String title, String templateName);
}
