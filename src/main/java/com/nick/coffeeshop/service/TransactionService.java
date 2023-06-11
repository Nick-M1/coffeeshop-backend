package com.nick.coffeeshop.service;

import com.nick.coffeeshop.exception.ResourceNotFoundException;
import com.nick.coffeeshop.model.Transaction;
import com.nick.coffeeshop.model.input.TransactionInput;
import com.nick.coffeeshop.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionItemService transactionItemService;
    private final UserService userService;

    public TransactionService(TransactionRepository transactionRepository, TransactionItemService transactionItemService, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.transactionItemService = transactionItemService;
        this.userService = userService;
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findOne(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
    }


    public Transaction create(String username, List<TransactionInput> transactionInput) {
        var transactionItems = transactionInput.stream()
                .map(transactionItemInput -> transactionItemService.create(transactionItemInput.productId(), transactionItemInput.quantity()))
                .toList();
        var user = userService.findByUsername(username);

        var transaction = new Transaction(user, OffsetDateTime.now(), transactionItems);
        var newTransaction = transactionRepository.save(transaction);

        log.info("Add transaction to DB: {}", newTransaction);
        return newTransaction;
    }

    @Transactional
    public Transaction update(Long id, List<TransactionInput> transactionInput) {
        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));

        if (transactionInput != null) {
            var transactionItems = transactionInput.stream()
                    .map(transactionItemInput -> transactionItemService.create(transactionItemInput.productId(), transactionItemInput.quantity()))
                    .toList();

            transaction.setTransactionItems(transactionItems);
            transaction.setTimestamp(OffsetDateTime.now());
        }

        return transaction;
    }


    public Transaction delete(Long id) {
        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
        transactionRepository.delete(transaction);
        return transaction;
    }
}
