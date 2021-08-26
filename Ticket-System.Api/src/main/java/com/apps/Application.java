package com.apps;

import com.apps.domain.repository.CityRepository;
import com.apps.peformance.InformationApp;
import com.apps.config.properties.ApplicationSecurityProperties;
import com.apps.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.apps.config.properties")
@Slf4j
@EnableScheduling
public class Application extends InformationApp implements CommandLineRunner {

    @Autowired
    private CityService cityService;

    @Autowired
    private ApplicationSecurityProperties properties;

    @Autowired
    private CityRepository cityRepository;

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class);
        Environment environment = applicationContext.getEnvironment();
        logApplicationStartup(environment);
    }

    public void run(String... args) {
        log.info(".... Fetching ");
        log.info("-->" + cityService.findByState("CA"));
        log.info("isbn-4567 -->" + cityService.findByState("CA"));
        log.info("isbn-1234 -->" + cityService.findByState("CA"));
        log.info("isbn-4567 -->" + cityService.findByState("CA"));
        log.info("isbn-1234 -->" + cityService.findByState("CA"));
        log.info("isbn-1234 -->" + cityService.findByState("CA"));
    }

}
