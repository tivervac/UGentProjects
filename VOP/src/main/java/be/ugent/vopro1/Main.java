package be.ugent.vopro1;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

/**
 * Starts the SpringApplication
 */
@SpringBootApplication
@ComponentScan("be.ugent.vopro1.rest.controller")
@Configuration
@ImportResource({"classpath*:**/application-context.xml"})
public class Main {

    /**
     * Empty constructor
     */
    public Main() {
        // No-op
    }

    /**
     * Starts the SpringApplication
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * Disable WRITE_DATES_AS_TIMESTAMPS
     *
     * @return ObjectMapperFactoryBean
     */
    @Bean
    public Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean() {
        Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
        bean.setFeaturesToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return bean;
    }
}
