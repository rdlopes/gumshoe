package org.rdlopes.gumshoe;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * User: ruilopes
 * Date: 30/04/2015
 * Time: 20:37
 */
@ComponentScan
@SpringBootApplication
public class GumshoeApplication {

    private static final Logger LOGGER = getLogger(GumshoeApplication.class);

    public static void main(String[] args) {
        // setup global properties
        System.setProperty("apple.awt.UIElement", "true");
        System.setProperty("java.awt.headless", "false");
        getDefaultToolkit();

        // start
        ConfigurableApplicationContext applicationContext = SpringApplication.run(GumshoeApplication.class, args);

        System.getProperties().stringPropertyNames()
                .stream()
                .sorted()
                .forEach(s -> LOGGER.info("System Property {}={}", s, System.getProperty(s)));
    }

}
