package com.nick.coffeeshop.config.commandlineRunner;

import com.nick.coffeeshop.repository.ProductRepository;
import com.nick.coffeeshop.repository.TransactionItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(11)
public class TransactionItemCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(TransactionItemCommandLineRunner.class);
    private final TransactionItemRepository transactionItemRepository;
    private final ProductRepository productRepository;

    public TransactionItemCommandLineRunner(TransactionItemRepository transactionItemRepository, ProductRepository productRepository) {
        this.transactionItemRepository = transactionItemRepository;
        this.productRepository = productRepository;
    }


    @Override
    public void run(String... args) throws Exception {
//        var transactionItem1 = new TransactionItem(productRepository.findById(1L).get(), 3);
//
//        transactionItemRepository.saveAll(List.of(transactionItem1))
//                .forEach(transactionItem -> logger.info("Add transaction-item to database: " + transactionItem.toString()));
    }
}
