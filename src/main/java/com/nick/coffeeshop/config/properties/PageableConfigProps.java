package com.nick.coffeeshop.config.properties;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("pageable-defaults")
@Validated
public record PageableConfigProps(
        @PositiveOrZero Integer defaultPageIndex,
        @Positive Integer defaultPageSize
) {
}
