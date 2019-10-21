package com.dtm.quicklearning.service;

import com.dtm.quicklearning.model.Mail;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface MailService {
    void sendEmailVerification(String emailVerificationUrl, String to) throws IOException, TemplateException, MessagingException;

    void sendResetLink(String resetPasswordLink, String to) throws IOException, TemplateException, MessagingException;

    void sendAccountChangeEmail(String action, String actionStatus, String to) throws IOException, TemplateException, MessagingException;

    void send(Mail mail) throws MessagingException;
}
