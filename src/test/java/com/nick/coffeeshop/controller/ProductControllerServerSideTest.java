package com.nick.coffeeshop.controller;

import com.nick.coffeeshop.config.GraphQlConfig;
import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.exception.ResourceNotFoundException;
import com.nick.coffeeshop.model.Product;
import com.nick.coffeeshop.service.ProductService;
import com.nick.coffeeshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Controller unit test - service is mocked & server not started:
 * <p>
 * Tests run on server-side, not via HTTP request.
 * <p>
 * (Doesn't require auth & doesn't need to package content-body as JSON)
 */

@Import({ GraphQlConfig.class })
@GraphQlTest(ProductController.class)
class ProductControllerServerSideTest {

    @Autowired GraphQlTester graphQlTester;

    @MockBean ProductService productService;
    @MockBean UserService userService;


    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = List.of(
                new Product("product1", ProductSize.SMALL, ProductType.COLD_DRINK, BigDecimal.valueOf(10), "img1"),
                new Product("product2", ProductSize.SMALL, ProductType.HOT_FOOD, BigDecimal.valueOf(3), "img2"),
                new Product("product3", ProductSize.MEDIUM, ProductType.SWEET_TREATS, BigDecimal.valueOf(1), "img3")
        );

        products.get(0).setId(1L);
        products.get(1).setId(2L);
        products.get(2).setId(3L);
    }


    @Test
    void testFindAllProductsShouldReturnAllProducts() {
        // language=GraphQL
        String document = """
            query {
                findAllProducts {
                    content {
                        id
                        name
                        productSize
                        productType
                        price
                        img
                    }
                    first
                    last
                    numberOfElements
                }
            }
        """;


        var page = new PageImpl<>(products);
        when(productService.findAllByExample(any(), any(), any(), any())).thenReturn(page);

        var response = graphQlTester
                .document(document)
                .execute();

        response.path("findAllProducts.content[*]")
                .hasValue()
                .entityList(Product.class)
                .hasSize(3)
                .contains(products.get(0), products.get(1), products.get(2));
    }

    @Test
    void shouldFindOneProduct() {
        // language=GraphQL
        String document = """
            query {
                findOneProduct(id: 1) {
                    id
                    name
                    productSize
                    productType
                    price
                    img
                }
            }
        """;

        when(productService.findOne(1L)).thenReturn(products.get(0));

        graphQlTester.document(document)
                .execute()
                .path("findOneProduct")
                .hasValue()
                .entity(Product.class)
                .isEqualTo(products.get(0));
    }

    @Test
    void shouldNotfindOneProduct() {
        // language=GraphQL
        String document = """
            query {
                findOneProduct(id: 10) {
                    id
                }
            }
        """;

        when(productService.findOne(10L)).thenThrow(new ResourceNotFoundException("Product", "id", 10L));

        var response = graphQlTester.document(document).execute();

        response.errors()
                .expect(r ->
                        Objects.equals(r.getMessage(), "Product with id=10 does not exist")
                        && r.getErrorType() == ErrorType.NOT_FOUND
                );

        response.path("findOneProduct").valueIsNull();
    }

    @Test
    void shouldCreateProduct() {
        // language=GraphQL
        String document = """
            mutation {
                createProduct(name: "product1", productSize: SMALL, productType: COLD_DRINK, price: 10, img: "img1") {
                    id
                    name
                    productSize
                    productType
                    price
                    img
                }
            }
        """;
        var product = products.get(0);
        when(productService.create(product.getName(), product.getProductSize(), product.getProductType(), product.getPrice(), product.getImg())).thenReturn(product);

        graphQlTester.document(document)
                .execute()
                .path("createProduct")
                .hasValue()
                .entity(Product.class)
                .isEqualTo(product);
    }

    @Test
    void shouldUpdateProduct() {
        // language=GraphQL
        String document = """
            mutation {
                updateProduct(id: 1, name: "new-name", productSize: LARGE, productType: HOT_DRINK, price: 20, img: "new-img") {
                    id
                    name
                    productSize
                    productType
                    price
                    img
                }
            }
        """;
        var product = new Product("new-name", ProductSize.LARGE, ProductType.HOT_DRINK, BigDecimal.valueOf(20), "new-img");
        product.setId(1L);

        when(productService.update(
                product.getId(),
                Optional.of(product.getName()),
                Optional.of(product.getProductSize()),
                Optional.of(product.getProductType()),
                Optional.of(product.getPrice()),
                Optional.of(product.getImg())
        )).thenReturn(product);

        graphQlTester.document(document)
                .execute()
                .path("updateProduct")
                .hasValue()
                .entity(Product.class)
                .isEqualTo(product);
    }

    @Test
    void shouldDeleteProduct() {
        // language=GraphQL
        String document = """
            mutation {
                deleteProduct(id: 1) {
                    id
                    name
                    productSize
                    productType
                    price
                    img
                }
            }
        """;

        var product = products.get(0);

        when(productService.delete(product.getId())).thenReturn(product);

        graphQlTester.document(document)
                .execute()
                .path("deleteProduct")
                .hasValue()
                .entity(Product.class)
                .isEqualTo(product);
    }

    @Test
    void shouldNotDeleteProduct() {
        // language=GraphQL
        String document = """
            mutation {
                deleteProduct(id: 10) {
                    id
                    name
                    productSize
                    productType
                    price
                    img
                }
            }
        """;
        var id = 10L;

        when(productService.delete(id)).thenThrow(new ResourceNotFoundException("Product", "id", id));

        var response = graphQlTester.document(document).execute();

        response.errors()
                .expect(r ->
                        Objects.equals(r.getMessage(), "Product with id=10 does not exist")
                                && r.getErrorType() == ErrorType.NOT_FOUND
                );

        response.path("deleteProduct").valueIsNull();
    }
}