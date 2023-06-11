package com.nick.coffeeshop.repository;

import com.nick.coffeeshop.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
