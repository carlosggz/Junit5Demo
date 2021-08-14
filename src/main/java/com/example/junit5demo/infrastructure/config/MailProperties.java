package com.example.junit5demo.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.mail")
@Data
public class MailProperties {
    String fromName;
    String fromAddress;
    List<String> toAddress;
    List<String> bccAddress;
    String subject;
}
