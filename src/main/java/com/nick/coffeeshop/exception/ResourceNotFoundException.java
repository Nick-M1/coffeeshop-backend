package com.nick.coffeeshop.exception;

import com.nick.coffeeshop.exception.handler.CustomException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
public class ResourceNotFoundException extends CustomException {
    public <T> ResourceNotFoundException(String itemName, String field, T id) {
        super(String.format("%s with %s=%s does not exist", itemName, field, id), ErrorType.NOT_FOUND);
    }
}
