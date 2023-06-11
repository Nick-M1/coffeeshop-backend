package com.nick.coffeeshop.controller;

import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.enums.SortBy;
import com.nick.coffeeshop.model.Product;
import com.nick.coffeeshop.model.filter.ProductFilter;
import com.nick.coffeeshop.service.ProductService;
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
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @QueryMapping
    public List<Product> findAllProduct() {
        return productService.findAll();
    }

    @QueryMapping
    public Page<Product> findAllProductByExample(
            @Argument Optional<ProductFilter> example,
            @Argument Optional<SortBy> sortBy,
            @Argument Optional<@PositiveOrZero Integer> page,
            @Argument Optional<@Positive Integer> size) {
        return productService.findAllByExample(example, sortBy, page, size);
    }


    @QueryMapping
    public Product findOneProduct(@Argument @PositiveOrZero Long id) {
        return productService.findOne(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(
            @Argument @Size(min = 2, max = 50) String name,
            @Argument ProductSize productSize,
            @Argument ProductType productType,
            @Argument BigDecimal price,
            @Argument String img) {
        return productService.create(name, productSize, productType, price, img);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(@Argument @PositiveOrZero Long id, @Argument @Size(min = 2, max = 50) String name, @Argument ProductSize productSize, @Argument ProductType productType) {
        return productService.update(id, name, productSize, productType);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product deleteProduct(@Argument @PositiveOrZero Long id) {
        return productService.delete(id);
    }
}
