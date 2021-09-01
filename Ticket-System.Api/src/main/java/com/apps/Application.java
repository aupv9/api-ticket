package com.apps;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.repository.CityRepository;
import com.apps.peformance.InformationApp;
import com.apps.config.properties.ApplicationSecurityProperties;
import com.apps.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;


@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.apps.config.properties")
@Slf4j
@EnableScheduling
public class Application extends InformationApp {

    @Autowired
    private CityService cityService;

    @Autowired
    private ApplicationSecurityProperties properties;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ApplicationCacheManager applicationCacheManager;

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class);
        Environment environment = applicationContext.getEnvironment();

        logApplicationStartup(environment);
    }


    @Bean
    ApplicationRunner applicationRunner(){
        return args -> {
            System.out.println(cacheManager.getClass().getName());
            log.info("1" + cityService.findByState("CA"));
            log.info("2" + cityService.findByState("CA"));
            log.info("3" + cityService.findByState("CA"));
            log.info("all keys: " +  applicationCacheManager.getKeys("CityService.findByState_CA"));

        };
    }

}
