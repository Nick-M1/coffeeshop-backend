package com.nick.coffeeshop.model.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

// For mutating / creating new transactions
public record TransactionInput(@PositiveOrZero Long productId, @Positive @Max(30) Integer quantity) {
}
