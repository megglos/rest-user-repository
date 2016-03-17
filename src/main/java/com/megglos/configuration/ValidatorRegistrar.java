package com.megglos.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Allows validator bena to get catched up automatically.
 *
 * Allows to use the the bean prefixes documented at
 * http://docs.spring.io/spring-data/rest/docs/current/reference/html/#validation
 * which didn't work out of the box, others share this problem:
 * https://jira.spring.io/browse/DATAREST-524
 * http://stackoverflow.com/questions/24318405/spring-data-rest-validator
 *
 * @author meggle
 */
@Configuration
public class ValidatorRegistrar implements InitializingBean {

    private static final List<String> EVENTS;
    static {
        List<String> events = new ArrayList<String>();
        events.add("beforeCreate");
        events.add("afterCreate");
        events.add("beforeSave");
        events.add("afterSave");
        events.add("beforeLinkSave");
        events.add("afterLinkSave");
        events.add("beforeDelete");
        events.add("afterDelete");
        EVENTS = Collections.unmodifiableList(events);
    }

    @Autowired
    ListableBeanFactory beanFactory;

    @Autowired
    ValidatingRepositoryEventListener validatingRepositoryEventListener;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Validator> validators = beanFactory.getBeansOfType(Validator.class);
        for (Map.Entry<String, Validator> entry : validators.entrySet()) {
            EVENTS.stream().filter(p -> entry.getKey().startsWith(p)).findFirst()
                    .ifPresent(p -> validatingRepositoryEventListener.addValidator(p, entry.getValue()));
        }
    }
}