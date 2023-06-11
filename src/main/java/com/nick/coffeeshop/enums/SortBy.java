package com.nick.coffeeshop.enums;

import org.springframework.data.domain.Sort;

public enum SortBy {
    NAME_ASC,
    NAME_DSC,
    PRICE_ASC,
    PRICE_DSC;

    public static Sort getSortRequest(SortBy sortBy) {
        return switch (sortBy) {
            case NAME_ASC -> Sort.by(Sort.Direction.ASC, "name");
            case NAME_DSC -> Sort.by(Sort.Direction.DESC, "name");
            case PRICE_ASC -> Sort.by(Sort.Direction.ASC, "price");
            case PRICE_DSC -> Sort.by(Sort.Direction.DESC, "price");
        };
    }
}
