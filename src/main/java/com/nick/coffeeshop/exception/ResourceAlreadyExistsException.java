package com.nick.coffeeshop.exception;

import com.nick.coffeeshop.exception.handler.CustomException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.graphql.execution.ErrorType;

@Getter
@Setter
public class ResourceAlreadyExistsException extends CustomException {
    public <T> ResourceAlreadyExistsException(String itemName, String field, T id) {
        super(String.format("%s with %s=%s already exists", itemName, field, id), ErrorType.BAD_REQUEST);
    }
}
