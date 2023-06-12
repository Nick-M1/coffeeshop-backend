package com.nick.coffeeshop.enums;

import org.springframework.data.domain.Sort;

public enum ProductSortBy {
    NAME_ASC,
    NAME_DSC,
    PRICE_ASC,
    PRICE_DSC;

    public static Sort getSortRequest(ProductSortBy productSortBy) {
        return switch (productSortBy) {
            case NAME_ASC -> Sort.by(Sort.Direction.ASC, "name");
            case NAME_DSC -> Sort.by(Sort.Direction.DESC, "name");
            case PRICE_ASC -> Sort.by(Sort.Direction.ASC, "price");
            case PRICE_DSC -> Sort.by(Sort.Direction.DESC, "price");
        };
    }
}
