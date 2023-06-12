package com.nick.coffeeshop.repository;

import com.nick.coffeeshop.model.Transaction;
import com.nick.coffeeshop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.user.username = :username")
    Page<Transaction> findAllByUsername(String username, Pageable pageable);
}
