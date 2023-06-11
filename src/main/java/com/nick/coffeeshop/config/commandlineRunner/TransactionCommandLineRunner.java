package com.nick.coffeeshop.config.commandlineRunner;

import com.nick.coffeeshop.repository.TransactionItemRepository;
import com.nick.coffeeshop.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(12)
public class TransactionCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(TransactionCommandLineRunner.class);
    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;

    public TransactionCommandLineRunner(TransactionRepository transactionRepository, TransactionItemRepository transactionItemRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionItemRepository = transactionItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        var transactionFull1 = new Transaction(OffsetDateTime.now(), List.of(transactionItemRepository.findById(1L).get()));
//
//        transactionRepository.saveAll(List.of(transactionFull1))
//                .forEach(transaction -> logger.info("Add transaction to database: " + transaction.toString()));
    }
}
