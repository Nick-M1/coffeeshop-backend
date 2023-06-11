package com.nick.coffeeshop.repository;

import com.nick.coffeeshop.model.Transaction;
import com.nick.coffeeshop.model.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {
}
