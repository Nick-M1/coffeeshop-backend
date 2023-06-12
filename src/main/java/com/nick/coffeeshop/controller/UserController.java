package com.nick.coffeeshop.controller;


import com.nick.coffeeshop.enums.Role;
import com.nick.coffeeshop.enums.UserSortBy;
import com.nick.coffeeshop.model.User;
import com.nick.coffeeshop.model.filter.UserFilter;
import com.nick.coffeeshop.service.UserService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Validated
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<User> findAllUsers(
            @Argument Optional<UserFilter> example,
            @Argument Optional<UserSortBy> sortBy,
            @Argument Optional<@PositiveOrZero Integer> page,
            @Argument Optional<@Positive Integer> size) {
        return userService.findAllByExample(example, sortBy, page, size);
    }


    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User findOneUser(@Argument @PositiveOrZero Long id) {
        return userService.findOne(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(
            @Argument @Size(min = 2, max = 50) String username,
            @Argument @Size(min = 2, max = 50) String password,
            @Argument Role role) {
        return userService.create(username, password, role);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(
            @Argument @PositiveOrZero Long id,
            @Argument Optional<@Size(min = 2, max = 50) String> username,
            @Argument Optional<Role> role) {
        return userService.update(id, username, role);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User deleteUser(@Argument @PositiveOrZero Long id) {
        return userService.delete(id);
    }
}
