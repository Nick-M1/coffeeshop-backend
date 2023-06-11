package com.nick.coffeeshop.model;

import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product_table")
public class Product {

    @Id @GeneratedValue
    private Long id;
    @Size(min = 2, max = 50)
    private String name;
    private ProductSize productSize;
    private ProductType productType;
    private BigDecimal price;
    private String img;

    public Product(String name, ProductSize productSize, ProductType productType, BigDecimal price, String img) {
        this.name = name;
        this.productSize = productSize;
        this.productType = productType;
        this.price = price;
        this.img = img;
    }

    public Product() {

    }
}
