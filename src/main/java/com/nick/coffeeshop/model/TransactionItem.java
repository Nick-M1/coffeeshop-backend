package com.nick.coffeeshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
@Table(name = "transactionitem_table")
public class TransactionItem {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
//    @JoinTable(name = "transactionitem_product", joinColumns = @JoinColumn(name="product_id", nullable=false))
//    @JoinColumn(name="product_id", nullable=false)
    private Product product;
    @PositiveOrZero @Max(25)
    private Integer quantity;

    public TransactionItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public TransactionItem() {
    }
}
