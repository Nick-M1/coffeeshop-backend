package com.nick.coffeeshop.exception;

import com.nick.coffeeshop.exception.handler.CustomException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.graphql.execution.ErrorType;

@Getter
@Setter
public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(message, ErrorType.BAD_REQUEST);
    }
}
