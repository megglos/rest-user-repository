package com.megglos.controller;

import com.megglos.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;

/**
 * Allows to add custom rest methods or customize the standard methods provided by spring-data-rest.
 *
 * @author meggle
 */
@RepositoryRestController
public class UserController {

    private final UserRepository repo;

    @Autowired
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

}
