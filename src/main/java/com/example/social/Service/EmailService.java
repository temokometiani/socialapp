package com.example.social.Service;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @PostConstruct
    public void init() {
        LOGGER.info("EmailService initialized with sender: {}", fromEmail);
        LOGGER.info("MailSender instance: {}", mailSender.getClass().getName());
    }

    @Async
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            LOGGER.debug("Attempting to send email to {}", to);
            mailSender.send(message);
            LOGGER.info("Email sent successfully to {}", to);
        } catch (MailException e) {
            LOGGER.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}