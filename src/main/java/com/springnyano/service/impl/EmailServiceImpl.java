package com.springnyano.service.impl;

import com.springnyano.entity.email.EmailEntity;
import com.springnyano.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Transactional

public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.from}")
    private String emailFrom;

    @Override
    public String sendTextEmail(EmailEntity email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getToEmail());
        message.setSubject(email.getSubject());
        message.setText(email.getMessageBody());
        message.setFrom(emailFrom);
        try {
            mailSender.send(message);
            System.out.println("Email send Successfully");
            return "Email sent Successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendHtmlEmail(EmailEntity email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailFrom);
            helper.setTo(email.getToEmail());
            helper.setSubject(email.getSubject());
            helper.setText(email.getMessageBody(), true);
            mailSender.send(message);
            System.out.println("Email send html Successfully");
            return "Email sent html Successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendMailAttachmentEmail(EmailEntity emails) {
        return "";
    }
}
