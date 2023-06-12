package com.nick.coffeeshop.service;


import com.nick.coffeeshop.config.properties.PageableConfigProps;
import com.nick.coffeeshop.enums.Role;
import com.nick.coffeeshop.enums.UserSortBy;
import com.nick.coffeeshop.exception.ResourceNotFoundException;
import com.nick.coffeeshop.model.User;
import com.nick.coffeeshop.model.filter.UserFilter;
import com.nick.coffeeshop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ExampleMatcher exampleMatcher;
    private final PageableConfigProps pageableConfigProps;

    public UserService(UserRepository userRepository, ExampleMatcher exampleMatcher, PageableConfigProps pageableConfigProps) {
        this.userRepository = userRepository;
        this.exampleMatcher = exampleMatcher;
        this.pageableConfigProps = pageableConfigProps;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public Page<User> findAllByExample(Optional<UserFilter> userExampleOptional, Optional<UserSortBy> sortBy, Optional<Integer> page, Optional<Integer> size) {
        var userExample = userExampleOptional.orElse(new UserFilter(null, null));

        Example<User> example = Example.of(
                new User(userExample.username(), null, userExample.role()),
                exampleMatcher
        );

        var sortRequest = UserSortBy.getSortRequest(sortBy.orElse(UserSortBy.USERNAME_ASC));
        var pageRequest = PageRequest.of(page.orElse(pageableConfigProps.defaultPageIndex()), size.orElse(pageableConfigProps.defaultPageSize()), sortRequest);

        return userRepository.findAll(example, pageRequest);
    }

    public User create(String username, String password, Role role) {
        var user = new User(username, password, role);
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, Optional<String> username, Optional<Role> role) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (username.isPresent() && !username.get().equals(user.getUsername()))
            user.setUsername(username.get());
        if (role.isPresent() && role.get() != user.getRole())
            user.setRole(role.get());

        return user;
    }

    public User delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
        return user;
    }
}
