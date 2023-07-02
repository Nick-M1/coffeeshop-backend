package com.nick.coffeeshop.repository;

import com.nick.coffeeshop.enums.Role;
import com.nick.coffeeshop.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }


    @Test
    void itShouldfindByUsername() {
        var username = "username";

        var user = new User(username, "password", Role.ADMIN);
        underTest.save(user);

        var foundUser = underTest.findByUsername(username);
        assertThat(foundUser).isNotEmpty().hasValue(user);
    }

    @Test
    void itShouldNotfindByUsername() {
        var username = "username";

        var foundUser = underTest.findByUsername(username);
        assertThat(foundUser).isEmpty();
    }
}