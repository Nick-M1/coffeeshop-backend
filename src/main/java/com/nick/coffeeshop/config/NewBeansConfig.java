package com.nick.coffeeshop.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;

@Configuration
public class NewBeansConfig {

    @Bean
    public Faker fakerDataInitialiser() {
        return new Faker();
    }

    @Bean
    public ExampleMatcher exampleMatcher() {
        return ExampleMatcher.matchingAll().withIgnoreCase();
    }
}
