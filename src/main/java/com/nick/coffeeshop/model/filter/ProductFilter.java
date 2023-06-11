package com.nick.coffeeshop.model.filter;

import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductType;

import java.math.BigDecimal;

// For querying products by example
public record ProductFilter(
        String name,
        ProductSize productSize,
        ProductType productType,
        BigDecimal price,
        String img
) {
}
