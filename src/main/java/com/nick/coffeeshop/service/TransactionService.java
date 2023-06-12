package com.nick.coffeeshop.service;

import com.nick.coffeeshop.config.properties.PageableConfigProps;
import com.nick.coffeeshop.exception.ResourceNotFoundException;
import com.nick.coffeeshop.model.Transaction;
import com.nick.coffeeshop.model.input.TransactionInput;
import com.nick.coffeeshop.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionItemService transactionItemService;
    private final UserService userService;
    private final PageableConfigProps pageableConfigProps;

    public TransactionService(TransactionRepository transactionRepository, TransactionItemService transactionItemService, UserService userService, PageableConfigProps pageableConfigProps) {
        this.transactionRepository = transactionRepository;
        this.transactionItemService = transactionItemService;
        this.userService = userService;
        this.pageableConfigProps = pageableConfigProps;
    }

    public Page<Transaction> findAll(Optional<Integer> page, Optional<Integer> size) {
        var pageRequest = PageRequest.of(
                page.orElse(pageableConfigProps.defaultPageIndex()),
                size.orElse(pageableConfigProps.defaultPageSize())
        );
        return transactionRepository.findAll(pageRequest);
    }

    public Page<Transaction> findAllByUser(String username, Optional<Integer> page, Optional<Integer> size) {
        var pageRequest = PageRequest.of(
                page.orElse(pageableConfigProps.defaultPageIndex()),
                size.orElse(pageableConfigProps.defaultPageSize())
        );
        return transactionRepository.findAllByUsername(username, pageRequest);
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
    public Transaction update(Long id, Optional<List<TransactionInput>> transactionInput) {
        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));

        if (transactionInput.isPresent()) {
            var transactionItems = transactionInput.get().stream()
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
