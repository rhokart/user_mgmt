package com.rhokart.usermgmt.service;

import com.rhokart.usermgmt.model.EmailDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public boolean sendEmailToUser(EmailDetails emailDetails) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(emailDetails.getToAddress());
            simpleMailMessage.setSubject(emailDetails.getSubject());
            simpleMailMessage.setText(emailDetails.getMsgBody());
            simpleMailMessage.setFrom(sender);

            javaMailSender.send(simpleMailMessage);

        } catch (Exception ex) {
            log.error("Error while sending email.");
            log.info("message:{}", ex.fillInStackTrace());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

