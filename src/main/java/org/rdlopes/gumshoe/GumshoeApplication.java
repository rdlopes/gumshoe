package org.rdlopes.gumshoe;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * User: ruilopes
 * Date: 30/04/2015
 * Time: 20:37
 */
@SpringBootApplication
@EnableConfigurationProperties(GumshoeProperties.class)
public class GumshoeApplication {

    private static final Logger log = getLogger(GumshoeApplication.class);

    public static void main(String[] args) {
        // setup global properties
        System.setProperty("apple.awt.UIElement", "true");
        System.setProperty("java.awt.headless", "false");
        getDefaultToolkit();

        // log properties
        System.getProperties().stringPropertyNames()
              .stream()
              .sorted()
              .forEach(s -> log.info("System Property {}={}", s, System.getProperty(s)));

        // start
        SpringApplication.run(GumshoeApplication.class, args);
    }

}
