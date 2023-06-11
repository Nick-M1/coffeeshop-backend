package com.nick.coffeeshop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "transaction_table")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_transactions", joinColumns = @JoinColumn(name="user_transaction_id", nullable=false))
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    private OffsetDateTime timestamp;
    @OneToMany
    @JoinTable(name = "transaction_list", joinColumns = @JoinColumn(name="transactionitem_id", nullable=false))
    private List<TransactionItem> transactionItems;


    public Transaction(User user, OffsetDateTime timestamp, List<TransactionItem> transactionItems) {
        this.user = user;
        this.timestamp = timestamp;
        this.transactionItems = transactionItems;
    }

    public Transaction() {
    }
}
