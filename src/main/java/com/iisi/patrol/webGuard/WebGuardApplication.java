package com.iisi.patrol.webGuard;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;

@EnableScheduling
@SpringBootApplication
public class WebGuardApplication {

    private static final Logger log = LoggerFactory.getLogger(WebGuardApplication.class);

    private final Environment env;

    public WebGuardApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
//		SpringApplication.run(WebApplication.class, args);
        SpringApplication app = new SpringApplication(WebGuardApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
                .ofNullable(env.getProperty("server.servlet.context-path"))
                .filter(StringUtils::isNotBlank)
                .orElse("/");
        log.info(
                "\n----------------------------------------------------------\n\t" +
                        "Application is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
//				env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
//				protocol,
//				hostAddress,
//				serverPort,
//				contextPath,
                env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }

}
