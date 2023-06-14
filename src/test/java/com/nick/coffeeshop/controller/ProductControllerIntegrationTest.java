package com.nick.coffeeshop.controller;

import com.nick.coffeeshop.config.GraphQlConfig;
import com.nick.coffeeshop.model.Product;
import com.nick.coffeeshop.repository.ProductRepository;
import com.nick.coffeeshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.graphql.test.tester.GraphQlTester;

@Import({ ProductService.class, GraphQlConfig.class })
@GraphQlTest(ProductController.class)           // Only focus on controller
class ProductControllerIntegrationTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    ProductService productService;


    @Test
    void testFindAllProductsShouldReturnAllProducts() {
        // language=GraphQL
        String document = """
            query {
                findAllProducts(example: { productSize: SMALL }) {
                    first
                    last
                    content {
                        id
                        name
                        productSize
                        productType
                    }
                }
            }
        """;

        graphQlTester.document(document)
                .execute()
                .path("findAllProducts.content[*]")
                .entityList(Product.class)
                .hasSize(0);
    }

    @Test
    void findOneProduct() {
    }

    @Test
    void createProduct() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }
}