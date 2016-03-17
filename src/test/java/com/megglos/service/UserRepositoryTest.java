package com.megglos.service;

import com.megglos.domain.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Do some repository unit testing.
 *
 * No custom impls at all just some sample tests to verify the custom methods added.
 *
 * @author meggle
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserRepositoryTestConfig.class)
public class UserRepositoryTest {

    @Autowired
    UserRepository cut;

    @After
    public void cleanUp() {
        cut.deleteAll();
    }

    @Test
    public void findByLastName() {
        User user = createAndPersistBob();

        List<User> result = cut.findByLastName("Vila");
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(user));
    }

    @Test
    public void findByFirstName() {
        User user = createAndPersistBob();

        List<User> result = cut.findByFirstName("Bob");
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(user));
    }

    @Test
    public void findByEmail() {
        User user = createAndPersistBob();

        User result = cut.findByEmail("bob@vila.com");
        assertThat(result, is(notNullValue()));
        assertThat(result, is(user));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testEmailUniqueConstraint() {
        createAndPersistBob();
        createAndPersistBob();
    }

    private User createAndPersistBob() {
        User bob = new User("Bob", "Vila", "bob@vila.com", "bobsSecret");
        cut.save(bob);
        return bob;
    }

}
