package com.nick.coffeeshop.model.filter;

import com.nick.coffeeshop.enums.Role;

public record UserFilter (
        String username,
        Role role
) {
}
