package com.nick.coffeeshop.controller;

import com.nick.coffeeshop.model.Transaction;
import com.nick.coffeeshop.model.input.TransactionInput;
import com.nick.coffeeshop.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Transaction> findAllTransactions(
            @Argument Optional<@PositiveOrZero Integer> page,
            @Argument Optional<@Positive Integer> size) {
        return transactionService.findAll(page, size);
    }

    @QueryMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public Page<Transaction> findAllTransactionsByUser(
            Principal principal,
            @Argument Optional<@PositiveOrZero Integer> page,
            @Argument Optional<@Positive Integer> size) {
        var username = principal.getName();
        return transactionService.findAllByUser(username, page, size);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Transaction findOneTransaction(@Argument @PositiveOrZero Long id) {
        return transactionService.findOne(id);
    }


    @MutationMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public Transaction createTransaction(
            Principal principal,
            @Argument @Valid List<TransactionInput> transactionInput) {
        return transactionService.create(principal.getName(), transactionInput);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Transaction updateTransaction(
            @Argument @PositiveOrZero Long id,
            @Argument @Valid Optional<List<TransactionInput>> transactionInput) {
        return transactionService.update(id, transactionInput);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Transaction deleteTransaction(@Argument @PositiveOrZero Long id) {
        return transactionService.delete(id);
    }
}
