package com.apps;


import com.apps.mybatis.mysql.UserRepository;
import com.apps.peformance.InformationApp;
import com.apps.config.properties.ApplicationSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;


@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.apps.config.properties")
public class Application extends InformationApp implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationSecurityProperties properties;
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class);
        Environment environment = applicationContext.getEnvironment();
        logApplicationStartup(environment);
    }
    public void run(String... args) throws Exception {
        System.out.println(userRepository.findByState("CA"));
    }
}
