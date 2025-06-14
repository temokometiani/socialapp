package com.example.social;

import com.example.social.Repository.UserRepository;
import com.example.social.model.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SocialApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SocialApplication.class, args);
        UserRepository bean = run.getBean(UserRepository.class);
        System.out.println(bean);




    }
}