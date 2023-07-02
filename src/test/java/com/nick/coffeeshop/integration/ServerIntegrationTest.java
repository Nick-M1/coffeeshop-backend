package com.nick.coffeeshop.integration;

import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.helpers.GraphQlTestHelper;
import jakarta.transaction.Transactional;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Transactional
public class ServerIntegrationTest {
    private final String GRAPHQL_POST_ENDPOINT = "/graphql";
    private final String AUTH_USERNAME = "admin";
    private final String AUTH_PASSWORD = "password";

    @Autowired
    private WebTestClient webClient;

    @Test
    public void shouldFindById() throws JSONException {
        var query = GraphQlTestHelper.getFindOneProductQuery();
        var variables = GraphQlTestHelper.getFindOneProductVariables(1L);

        // language=json
        String expectedResponseJson = """
            {
                "data": {
                    "findOneProduct": {
                        "id": "1",
                        "name": "Caffee",
                        "productSize": "SMALL",
                        "productType": "HOT_DRINK",
                        "price": 3.2,
                        "img": "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg/1200px-Good_Food_Display_-_NCI_Visuals_Online.jpg"
                    }
                }
            }
        """;

        performGraphqlPost(query, variables, true)
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.errors").doesNotExist()
                .json(expectedResponseJson, true);
    }

    @Test
    public void shouldNotFindById() throws JSONException {
        var query = GraphQlTestHelper.getFindOneProductQuery();
        var variables = GraphQlTestHelper.getFindOneProductVariables(10L);

        performGraphqlPost(query, variables, true)
                .expectBody()
                .jsonPath("$.errors").exists()
                .jsonPath("$.errors[*].message").isEqualTo("Product with id=10 does not exist")
                .jsonPath("$.errors[*].extensions.classification").isEqualTo(ErrorType.NOT_FOUND.name())
                .jsonPath("$.data.findOneProduct").isEmpty();
    }

    @Test
    public void shouldFindAllProducts() throws JSONException {
        var query = GraphQlTestHelper.getFindAllProductsQuery();
        var variables = GraphQlTestHelper.getFindAllProductsVariables(null, null, null, null,  null, null, null, null);

        // language=json
        String expectedResponseJson = """
            {"data":{"findAllProducts":{"content":[{"id":"1","name":"Caffee","productSize":"SMALL","productType":"HOT_DRINK","price":3.20,"img":"https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg/1200px-Good_Food_Display_-_NCI_Visuals_Online.jpg"},{"id":"2","name":"Caffee2","productSize":"LARGE","productType":"COLD_DRINK","price":3.60,"img":"https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Nice_Cup_of_Tea.jpg/390px-Nice_Cup_of_Tea.jpg"}],"first":true,"last":true,"number":0,"numberOfElements":2,"totalElements":2,"totalPages":1,"pageable":{"pageNumber":0,"pageSize":20}}}}
        """;

        performGraphqlPost(query, variables, true)
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.errors").doesNotExist()
                .jsonPath("$.data.findAllProducts.content").isArray()
                .json(expectedResponseJson, false);         // Note: If this fails, then do to @Transactional not working so only check each field seperately
    }

    @Test
    public void shouldFindAllProductsReturnsEmpty() throws JSONException {
        var query = GraphQlTestHelper.getFindAllProductsQuery();
        var variables = GraphQlTestHelper.getFindAllProductsVariables("random-name", null, null, null,  null, null, null, null);

        // language=json
        String expectedResponseJson = """
            {"data":{"findAllProducts":{"content":[],"first":true,"last":true,"number":0,"numberOfElements":0,"totalElements":0,"totalPages":0,"pageable":{"pageNumber":0,"pageSize":20}}}}
        """;

        performGraphqlPost(query, variables, true)
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.errors").doesNotExist()
                .jsonPath("$.data.findAllProducts.content").isArray()
                .json(expectedResponseJson, true);
    }

    @Test
    public void shouldCreateProduct() throws JSONException {
        var query = GraphQlTestHelper.getCreateProductsQuery();
        var variables = GraphQlTestHelper.getCreateProductsVariables("name", ProductSize.SMALL, ProductType.HOT_FOOD, BigDecimal.TEN, "img");

        // language=json
        String expectedResponseJson = """
            {"data":{"createProduct":{"name":"name","productSize":"SMALL","productType":"HOT_FOOD","price":10,"img":"img"}}}
        """;

        performGraphqlPost(query, variables, true)
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.errors").doesNotExist()
                .json(expectedResponseJson, false);
    }

    @Test
    public void shouldNotCreateProduct_NoAuth() throws JSONException {
        var query = GraphQlTestHelper.getCreateProductsQuery();
        var variables = GraphQlTestHelper.getCreateProductsVariables("name", ProductSize.SMALL, ProductType.HOT_FOOD, BigDecimal.TEN, "img");

        performGraphqlPost(query, variables, false)
                .expectStatus().isUnauthorized();
    }





    // Helper functions:
    private WebTestClient.ResponseSpec performGraphqlPost(String query, Map<String, ?> variables, boolean isAuthenticated) throws JSONException {
        return webClient.post()
                .uri(GRAPHQL_POST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(generateRequest(query, variables))
                .header(HttpHeaders.AUTHORIZATION, isAuthenticated ? "Basic " + HttpHeaders.encodeBasicAuth(AUTH_USERNAME, AUTH_PASSWORD, null) : "Basic AA")
                .exchange();
    }

    private String generateRequest(String query, @Nullable Map<String, ?> variables) throws JSONException {
        var jsonObject = new JSONObject();
        jsonObject.put("query", query);

        if (variables != null)
            jsonObject.put("variables", new JSONObject(variables));

        return jsonObject.toString();
    }
}
