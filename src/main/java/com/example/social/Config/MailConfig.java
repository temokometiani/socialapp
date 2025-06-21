package com.example.social.Config;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {


    @Bean
    @ConfigurationProperties(prefix = "spring.mail")
    public JavaMailSenderImpl mailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public MailProperties mailProperties() {
        return new MailProperties();
    }
}