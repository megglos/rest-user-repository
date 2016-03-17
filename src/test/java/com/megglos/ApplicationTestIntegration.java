package com.megglos;

import com.megglos.service.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTestIntegration {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository repo;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        repo.deleteAll();
    }

    @Test
    public void testBaseUri() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(containsString("users")));
    }

    @Test
    public void createUserSuccess() throws Exception {
        String redirectUrl = createBob();

        this.mvc.perform(get(redirectUrl))
                .andExpect(jsonPath("$.firstName").value("Frodo"))
                .andExpect(jsonPath("$.lastName").value("Baggins"))
                .andExpect(jsonPath("$.email").value("frodo@baggins.com"))
                .andExpect(jsonPath("$.password").value(not("test")));
    }

    @Test
    public void createUserMissingFirstName() throws Exception {
        this.mvc.perform(
                post("/users").contentType(MediaType.APPLICATION_JSON).content(
                        "{  \"lastName\" : \"Baggins\", \"email\" : \"frodo@baggins.com\", \"password\" : \"test\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].entity").value("User"))
                .andExpect(jsonPath("$.errors[0].property").value("firstName"));
    }

    @Test
    public void createUserMissingLastName() throws Exception {
        this.mvc.perform(
                post("/users").contentType(MediaType.APPLICATION_JSON).content(
                        "{  \"firstName\" : \"Frodo\", \"email\" : \"frodo@baggins.com\", \"password\" : \"test\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].entity").value("User"))
                .andExpect(jsonPath("$.errors[0].property").value("lastName"));
    }

    @Test
    public void createUserMissingEmail() throws Exception {
        this.mvc.perform(
                post("/users").contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"firstName\" : \"Frodo\", \"lastName\" : \"Baggins\", \"password\" : \"test\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].entity").value("User"))
                .andExpect(jsonPath("$.errors[0].property").value("email"));
    }

    @Test
    public void createUserMissingPassword() throws Exception {
        this.mvc.perform(
                post("/users").contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"firstName\" : \"Frodo\", \"lastName\" : \"Baggins\", \"email\" : \"frodo@baggins.com\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].entity").value("User"))
                .andExpect(jsonPath("$.errors[0].property").value("password"));
    }

    @Test
    public void rejectDuplicateEmail() throws Exception {
        createBob();

        this.mvc.perform(
                post("/users").contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"firstName\" : \"Frodo\", \"lastName\" : \"Baggins\", \"email\" : \"frodo@baggins.com\", \"password\" : \"test\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].entity").value("User"))
                .andExpect(jsonPath("$.errors[0].property").value("email"));
    }

    @Test
    public void fullyUpdateUser() throws Exception {
        String resourceUrl = createBob();

        this.mvc.perform(
                put(resourceUrl).contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"firstName\" : \"Drogo\", \"lastName\" : \"Baggins\", \"email\" : \"drogo@baggins.com\", \"password\" : \"test\" }"))
                .andExpect(status().isNoContent())
                .andExpect(redirectedUrlPattern("**/users/*"));

        this.mvc.perform(get(resourceUrl))
                .andExpect(jsonPath("$.firstName").value("Drogo"))
                .andExpect(jsonPath("$.lastName").value("Baggins"))
                .andExpect(jsonPath("$.email").value("drogo@baggins.com"))
                .andExpect(jsonPath("$.password").value(not("test")));
    }

    @Test
    public void partiallyUpdateUser() throws Exception {
        String resourceUrl = createBob();

        this.mvc.perform(
                patch(resourceUrl).contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"email\" : \"frodo2@baggins.com\" }"))
                .andExpect(status().isNoContent());

        this.mvc.perform(get(resourceUrl))
                .andExpect(jsonPath("$.firstName").value("Frodo"))
                .andExpect(jsonPath("$.lastName").value("Baggins"))
                .andExpect(jsonPath("$.email").value("frodo2@baggins.com"))
                .andExpect(jsonPath("$.password").value(not("test")));
    }

    @Test
    public void deleteUser() throws Exception {
        String resourceUrl = createBob();

        this.mvc.perform(
                delete(resourceUrl).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        this.mvc.perform(get(resourceUrl)).andExpect(status().isNotFound());
    }

    private String createBob() throws Exception {
        return this.mvc.perform(
                post("/users").contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"firstName\" : \"Frodo\", \"lastName\" : \"Baggins\", \"email\" : \"frodo@baggins.com\", \"password\" : \"test\" }"))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/users/*"))
                .andReturn().getResponse().getRedirectedUrl();
    }

}
