package com.sinnet.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This guy is busy, nothing left.
 *
 * @author John Zhang
 */
@RestController
@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    @Autowired
    private HelloProperties helloProperties;

    @Value("${spring.cloud.kubernetes.config.name:''}")
    private String configMapName;

    @Scheduled(fixedDelay = 5000)
    public void printProperties() {
        System.out.println("configMapName = " + configMapName);
        System.out.println("helloProperties = " + helloProperties);;
    }

    private static final String INDEX_TEMPLATE =
            "<center style=\"padding:10px;background-color:#e0e0e0\">" +
            "  <h1>%s</h1>" +
            "  <h2>You are visiting version: <span style=\"color:%s\">%s</span> app</h2>" +
            "</center>";

    @GetMapping("/")
    public String hello(String name) {
        String message = "Hello world";
        if (!StringUtils.isEmpty(name)) {
            message = "Hello " + name;
        }
        return String.format(INDEX_TEMPLATE, message, helloProperties.getColor(), helloProperties.getVersion());
    }
}
