package com.nick.coffeeshop.controller;

import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.model.Product;
import com.nick.coffeeshop.model.User;
import com.nick.coffeeshop.service.ProductService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
/**
 * Controller unit test - service is mocked (but still requires whole server to run to access auth DB):
 * <p>
 * Tests run on a mocked client via HTTP request (not server-side).
 * <p>
 * (Requires auth & needs to package content-body as JSON)
 */

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ProductControllerMockMvcTest {
    private final String GRAPHQL_POST_ENDPOINT = "/graphql";
    private final String AUTH_USERNAME = "admin";
    private final String AUTH_PASSWORD = "password";


    @Autowired private MockMvc mockMvc;
//    @MockBean private ProductService productService;

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
    public void shouldFindOneProduct() throws Exception {
//        // language=GraphQL
//        String document = """
//            query {
//                findAllProducts {
//                    content {
//                        id
//                        name
//                        productSize
//                        productType
//                        price
//                        img
//                    }
//                    first
//                    last
//                    numberOfElements
//                }
//            }
//        """;
        // language=GraphQL
        String document = """
            query {
                findOneProduct(id: 1) {
                    id
                    name
                }
            }
        """;

        var product = products.get(0);
//        when(productService.findOne(any())).thenReturn(product);

        performGraphqlPost(document)
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.data.findOneProduct.id").value(1))
                .andDo(print())
        ;

//        performGraphqlPost(document)
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.errors").doesNotExist())
//                .andExpect(jsonPath("$.data.findAllProducts.first").value(true))
//                .andDo(print())
//        ;

    }




    // Helper functions:
    private ResultActions performGraphqlPost(String query) throws Exception {
        return performGraphqlPost(query, null);
    }

    private ResultActions performGraphqlPost(String query, Map<String, ?> variables) throws Exception {
        return mockMvc.perform(
                post(GRAPHQL_POST_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
                        .content(generateRequest(query, variables))
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("admin", "password", null))
        );
    }

    private String generateRequest(String query, @Nullable Map<String, ?> variables) throws JSONException {
        var jsonObject = new JSONObject();
        jsonObject.put("query", query);

        if (variables != null)
            jsonObject.put("variables", new JSONObject(variables));

        return jsonObject.toString();
    }
}
