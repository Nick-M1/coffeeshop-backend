package com.nick.coffeeshop.repository;

import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.model.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.productType = :productFilter")
    Page<Product> findProductsByQuery(ProductType productFilter, Pageable pageable);
}
