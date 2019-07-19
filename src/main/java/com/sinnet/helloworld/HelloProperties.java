package com.sinnet.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This guy is busy, nothing left.
 *
 * @author John Zhang
 */
@Configuration
@ConfigurationProperties(prefix = "example")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HelloProperties {

    private String version = "v1";
    private String color = "orange";
}
