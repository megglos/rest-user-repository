package com.megglos.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configures general JSR303 validation.
 * <p>
 * Wasn't active be default, always resulted in HTTP 500 instead of 400.
 * https://jira.spring.io/browse/DATAREST-370
 *
 * Gets catched up and registered by {@link com.megglos.configuration.ValidatorRegistrar} by the bean name prefixes.
 *
 * @author meggle
 */
@Configuration
public class Jsr303ValidationConfiguration {


    @Bean
    @Primary
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }


    @Bean
    Validator beforeCreateJsr303Validator() {
        return validator();
    }

    @Bean
    Validator beforeSaveJsr303Validator() {
        return validator();
    }

}