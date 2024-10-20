package com.springnyano.service;

import com.springnyano.entity.email.EmailEntity;

public interface EmailService {

    String sendTextEmail(EmailEntity email);

    String sendHtmlEmail(EmailEntity email);

    String sendMailAttachmentEmail(EmailEntity emails);
}
