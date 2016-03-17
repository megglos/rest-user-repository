package com.megglos.controller;

import com.megglos.domain.User;
import com.megglos.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * Used to perform additional actions on the data received by the RepositoryRestController for the User domain.
 *
 * @author meggle
 */
@Component
@RepositoryEventHandler
public class UserEventHandler {

    private final EncryptionService encSvc;

    @Autowired
    public UserEventHandler(EncryptionService encSvc) {
        this.encSvc = encSvc;
    }

    /**
     * Encrypt the pw as the plain text is received.
     *
     * @param user
     */
    @HandleBeforeCreate
    @HandleBeforeSave
    public void encryptPassword(User user) {
        user.setPassword(encSvc.encrypt(user.getPassword()));
    }

}
