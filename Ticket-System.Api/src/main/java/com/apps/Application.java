package com.apps;

import com.apps.authenticate.service.UserAccountStatusService;
import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.repository.CityRepository;
import com.apps.config.properties.ApplicationSecurityProperties;
import com.apps.authenticate.repository.UserAccountRepository;
import com.apps.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties
@Slf4j
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Autowired
    private ApplicationSecurityProperties properties;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ApplicationCacheManager applicationCacheManager;


    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountStatusService accountStatusService;

    public static void main(String[] args) {
         ApplicationContext applicationContext = SpringApplication.run(Application.class);
            Environment environment = applicationContext.getEnvironment();
            logApplicationStartup(environment);
    }

    static void logApplicationStartup(Environment env){
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        if (CommonUtils.isNullOrEmpty(serverPort)) {
            serverPort = "8080";
        }
        String contextPath = env.getProperty("server.servlet.context-path");
        if (CommonUtils.isNullOrEmpty(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());

    };

    @Bean
    ApplicationRunner applicationRunner(){
        return args -> {

//            UserAccountStatus accountStatus = new UserAccountStatus();
//            accountStatus.setCode("CFE");
//            accountStatus.setName("CONFIRMED");
//            accountStatus.setId(1);
//            log.info("insert user_account_status :"+ accountStatusService.insert(accountStatus));
//            log.info("select all user_account_status :"+  accountStatusService.findById(2));
//            log.info("select all user_account_status :"+  accountStatusService.findById(1));
//            log.info("select all user_account_status :"+  accountStatusService.findAll());
//            log.info("select all user_account_status :"+  accountStatusService.findAll());
//            log.info("select all user_account_status :"+  accountStatusService.findAll());
//            log.info("select all user_account_status :"+  accountStatusService.findAll());

//            log.info("Select city: " + cityService.findByState("CA"));
//            log.info("Select city: " + cityService.findByState("CA"));
//            log.info("Select city: " + cityService.findByState("CA"));
//            log.info("Select city: " + cityService.findByState("CA"));
//            log.info("Select city: " + cityService.findByState("CA"));

        };
    }




}
