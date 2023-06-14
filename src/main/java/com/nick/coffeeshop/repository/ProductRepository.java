package com.nick.coffeeshop.repository;

import com.nick.coffeeshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
