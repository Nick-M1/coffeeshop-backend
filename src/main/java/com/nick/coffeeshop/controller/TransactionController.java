package com.nick.coffeeshop.controller;

import com.nick.coffeeshop.model.Transaction;
import com.nick.coffeeshop.model.input.TransactionInput;
import com.nick.coffeeshop.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.util.List;

@Controller
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Transaction> findAllTransaction() {
        return transactionService.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Transaction> findAllTransactionByUser(Principal principal) {
        var username = principal.getName();
        return transactionService.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Transaction findOneTransaction(@Argument Long id) {
        return transactionService.findOne(id);
    }



    @MutationMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public Transaction createTransaction(Principal principal, @Argument @Valid List<TransactionInput> transactionInput) {
        return transactionService.create(principal.getName(), transactionInput);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Transaction updateTransaction(@Argument @PositiveOrZero Long id, @Argument @Valid List<TransactionInput> transactionInput) {
        return transactionService.update(id, transactionInput);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Transaction deleteTransaction(@Argument @PositiveOrZero Long id) {
        return transactionService.delete(id);
    }
}
