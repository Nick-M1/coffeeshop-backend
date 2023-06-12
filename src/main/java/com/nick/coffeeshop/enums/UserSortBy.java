package com.nick.coffeeshop.enums;

import org.springframework.data.domain.Sort;

public enum UserSortBy {
    USERNAME_ASC,
    USERNAME_DSC,
    ROLE_ASC,
    ROLE_DSC;

    public static Sort getSortRequest(UserSortBy productSortBy) {
        return switch (productSortBy) {
            case USERNAME_ASC -> Sort.by(Sort.Direction.ASC, "username");
            case USERNAME_DSC -> Sort.by(Sort.Direction.DESC, "username");
            case ROLE_ASC -> Sort.by(Sort.Direction.ASC, "role");
            case ROLE_DSC -> Sort.by(Sort.Direction.DESC, "role");
        };
    }
}
