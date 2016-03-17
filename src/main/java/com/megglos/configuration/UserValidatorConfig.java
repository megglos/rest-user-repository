package com.megglos.configuration;

import com.megglos.domain.User;
import com.megglos.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Adds custom validators for user domain.
 * <p>
 * Uses bean name prefixes to register validators, see:
 * http://docs.spring.io/spring-data/rest/docs/current/reference/html/#validation
 *
 * @author meggle
 */
@Configuration
public class UserValidatorConfig extends RepositoryRestConfigurerAdapter {

    @Autowired
    private UserRepository repo;

    @Bean
    Validator beforeCreateUserValidator() {
        return new DuplicateMailValidator(repo);
    }

    /**
     * Validates whether the given user mail already exists.
     */
    class DuplicateMailValidator implements Validator {

        private final UserRepository repo;

        public DuplicateMailValidator(UserRepository repo) {
            this.repo = repo;
        }

        @Override
        public boolean supports(Class<?> aClass) {
            return User.class.isAssignableFrom(aClass);
        }

        @Override
        public void validate(Object o, Errors errors) {
            if (repo.findByEmail(((User) o).getEmail()) != null) {
                errors.rejectValue("email", "email.duplicate", "User with given email already exists");
            }
        }
    }

}
