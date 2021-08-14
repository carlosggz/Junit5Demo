package com.example.junit5demo.integration;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(properties = {
        "spring.flyway.baseline-version=0",
        "spring.flyway.locations=classpath:db/migration/postgres",
        "spring.flyway.sql-migration-prefix=V",
        "spring.flyway.table=demo_schema_version",
        "spring.flyway.schemas=demo",
        "spring.flyway.init-sqls=CREATE SCHEMA IF NOT EXISTS demo;",
        "spring.flyway.user=demo",
        "spring.flyway.password=demo",
        "spring.datasource.driver-class-name=org.postgresql.Driver",
        "spring.datasource.url=jdbc:postgresql://localhost:5555/demo",
        "spring.datasource.userName=demo",
        "spring.datasource.password=demo",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",
        "spring.jpa.properties.hibernate.default_schema=demo"
})
@Testcontainers
class FlywayTests {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11")
            .withDatabaseName("demo")
            .withUsername("demo")
            .withPassword("demo")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5555), new ExposedPort(5432)))
            ));

    static {
        postgreSQLContainer.start();
    }

    @Test
    void contextLoads() {
    }

}
