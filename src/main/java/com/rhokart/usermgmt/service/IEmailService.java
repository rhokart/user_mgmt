package com.rhokart.usermgmt.service;

import com.rhokart.usermgmt.model.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface IEmailService {
    boolean sendEmailToUser(EmailDetails details);
}
