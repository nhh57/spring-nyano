package com.springnyano.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springnyano.entity.email.EmailEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class KafkaService {
    @Autowired
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "otp-auth-topic", groupId = "otp-group-id")
    public void listenOTP(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            /**
             * jsonNode
             * email
             * otp
             *
             */

            String email = jsonNode.get("email").asText();
            String otp = jsonNode.get("otp").asText();
            log.info("otp is {}, email is {}", otp, email);
            EmailEntity emailEntity = EmailEntity.builder()
                    .toEmail(email)
                    .subject("Send OTP FROM KAFKA GO")
                    .messageBody("OTP IS" + otp)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
