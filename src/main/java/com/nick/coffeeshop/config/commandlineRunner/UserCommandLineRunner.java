package com.nick.coffeeshop.config.commandlineRunner;

import com.github.javafaker.Faker;
import com.nick.coffeeshop.enums.Role;
import com.nick.coffeeshop.model.User;
import com.nick.coffeeshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(10)
public class UserCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(UserCommandLineRunner.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker;

    public UserCommandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder, Faker faker) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.faker = faker;
    }


    @Override
    public void run(String... args) {
        var user1 = new User("admin", passwordEncoder.encode("password"), Role.ADMIN);
        var user2 = new User("customer", passwordEncoder.encode("password"), Role.CUSTOMER);
        var user3 = new User("customer2", passwordEncoder.encode("password"), Role.CUSTOMER);

        userRepository.saveAll(List.of(user1, user2, user3))
            .forEach(user -> logger.info("Add user to database: " + user.toString()));
    }
}
