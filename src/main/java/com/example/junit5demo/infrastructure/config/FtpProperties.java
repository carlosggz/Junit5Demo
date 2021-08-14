package com.example.junit5demo.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.ftp")
@Data
public class FtpProperties {
    String serverName;
    int port;
    String userName;
    String password;
}
