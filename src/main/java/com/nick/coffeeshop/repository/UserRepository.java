package com.nick.coffeeshop.repository;

import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.model.Product;
import com.nick.coffeeshop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
