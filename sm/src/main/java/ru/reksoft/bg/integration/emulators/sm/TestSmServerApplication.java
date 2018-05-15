package ru.reksoft.bg.integration.emulators.sm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = {"ru.reksoft.bg.integration.emulators.sm"})
public class TestSmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSmServerApplication.class, args);

    }
}
