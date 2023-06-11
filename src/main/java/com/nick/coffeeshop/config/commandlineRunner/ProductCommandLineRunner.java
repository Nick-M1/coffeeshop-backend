package com.nick.coffeeshop.config.commandlineRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.model.Product;
import com.nick.coffeeshop.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

@Component
@Order(10)
public class ProductCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ProductCommandLineRunner.class);
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final Faker faker;

    @Value("${dataloader.userealdata.products}")
    private boolean USE_JSON_DATA;

    public ProductCommandLineRunner(ProductRepository productRepository, ObjectMapper objectMapper, Faker faker) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.faker = faker;
    }


    @Override
    public void run(String... args) {
        if (USE_JSON_DATA)
            loadRealProductsData();
        else
            loadFakeProductsData();
    }

    // Load real data from "/data/initial-products-data.json"
    private void loadRealProductsData() {
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/initial-products-data.json")) {
            var products = objectMapper.readValue(inputStream, new TypeReference<List<Product>>() {});
            productRepository.saveAll(products)
                    .forEach(product -> logger.info("Add product to database: " + product.toString()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Load fake/mock data using Java-Faker
    private void loadFakeProductsData() {
        var productSizes = List.of(ProductSize.SMALL, ProductSize.MEDIUM, ProductSize.LARGE);
        var productTypesFood = List.of(ProductType.COLD_FOOD, ProductType.HOT_FOOD, ProductType.SWEET_TREATS);
        var productTypesDrinks = List.of(ProductType.COLD_DRINK, ProductType.HOT_DRINK);

        var productsFood = IntStream.rangeClosed(1, 20)
                .mapToObj(i -> new Product(
                        faker.food().dish(),
                        productSizes.get(i % 3),
                        productTypesFood.get(i % 3),
                        BigDecimal.valueOf(3 + i / 10),
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg/1200px-Good_Food_Display_-_NCI_Visuals_Online.jpg"
                )).toList();

        var productsDrinks = IntStream.rangeClosed(1, 20)
                .mapToObj(i -> new Product(
                        faker.beer().name(),
                        productSizes.get(i % 3),
                        productTypesDrinks.get(i % 2),
                        BigDecimal.valueOf(3 + i / 10),
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Nice_Cup_of_Tea.jpg/390px-Nice_Cup_of_Tea.jpg"
                )).toList();

        productRepository.saveAll(productsFood)
                .forEach(product -> logger.info("Add product to database: " + product.toString()));
        productRepository.saveAll(productsDrinks)
                .forEach(product -> logger.info("Add product to database: " + product.toString()));
    }
}
