package com.apps;

import com.apps.contants.Utilities;
import com.apps.utils.CommonUtils;


import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.*;
import java.time.format.DateTimeFormatter;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableCaching
@EnableConfigurationProperties
@Slf4j
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

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
            DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
//            Instant instant = Instant.parse("2021-10-28T17:05:22.523Z");
//            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
//            ZonedDateTime zdt = instant.atZone(zoneId);
//            Timestamp  timestamp = Timestamp.from(instant);
//            System.out.println(timestamp);

//            System.out.println(Utilities.startOfWeek("yyyy-MM-dd"));;
//            System.out.println(Utilities.currentWeekEndDate());;

//            Snowflake snowflake = new Snowflake(1, 1);
//            for (int i = 0; i < 100; i++) {
//                long id = snowflake.nextId();
//                System.out.println(id);
//            }
        };
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }



}
