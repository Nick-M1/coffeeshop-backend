package com.nick.coffeeshop.exception.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.graphql.execution.ErrorType;

@Getter
@Setter
abstract public class CustomException extends RuntimeException {
    private ErrorType errorType;

    public CustomException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
