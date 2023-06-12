package com.nick.coffeeshop.service;

import com.nick.coffeeshop.config.properties.PageableConfigProps;
import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductSortBy;
import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.exception.ResourceNotFoundException;
import com.nick.coffeeshop.model.Product;
import com.nick.coffeeshop.model.filter.ProductFilter;
import com.nick.coffeeshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ExampleMatcher exampleMatcher;
    private final PageableConfigProps pageableConfigProps;

    public ProductService(ProductRepository productRepository, ExampleMatcher exampleMatcher, PageableConfigProps pageableConfigProps) {
        this.productRepository = productRepository;
        this.exampleMatcher = exampleMatcher;
        this.pageableConfigProps = pageableConfigProps;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAllByExample(Optional<ProductFilter> productExampleOptional, Optional<ProductSortBy> sortBy, Optional<Integer> page, Optional<Integer> size) {
        var productExample = productExampleOptional.orElse(new ProductFilter(null, null, null, null, null));

        Example<Product> example = Example.of(
                new Product(productExample.name(), productExample.productSize(), productExample.productType(), productExample.price(), productExample.img()),
                exampleMatcher
        );

        var sortRequest = ProductSortBy.getSortRequest(sortBy.orElse(ProductSortBy.NAME_ASC));
        var pageRequest = PageRequest.of(page.orElse(pageableConfigProps.defaultPageIndex()), size.orElse(pageableConfigProps.defaultPageSize()), sortRequest);

        return productRepository.findAll(example, pageRequest);
    }


    public Product findOne(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        log.info("Find product by id={}: {}", id, product);
        return product;
    }

    public Product create(String name, ProductSize productSize, ProductType productType, BigDecimal price, String img) {
        var product = new Product(name, productSize, productType, price, img);
        log.info("Add product to DB: {}", product);

        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, Optional<String> name, Optional<ProductSize> productSize, Optional<ProductType> productType) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (name.isPresent() && !name.get().equals(product.getName()))
            product.setName(name.get());
        if (productSize.isPresent() && productSize.get() != product.getProductSize())
            product.setProductSize(productSize.get());
        if (productType.isPresent() && productType.get() != product.getProductType())
            product.setProductType(productType.get());

        return product;
    }

    public Product delete(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
        return product;
    }
}
