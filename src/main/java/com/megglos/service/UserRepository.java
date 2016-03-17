package com.megglos.service;

import com.megglos.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * User Repository providing default spring-data-rest functionality.
 *
 * @author meggle
 */
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByLastName(@Param("lastName") String lastName);
    List<User> findByFirstName(@Param("firstName") String firstName);
    User findByEmail(@Param("email") String email);

}
