package com.nick.coffeeshop.service;

import com.nick.coffeeshop.exception.ResourceNotFoundException;
import com.nick.coffeeshop.model.TransactionItem;
import com.nick.coffeeshop.repository.TransactionItemRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TransactionItemService {

    private final TransactionItemRepository transactionItemRepository;
    private final ProductService productService;

    public TransactionItemService(TransactionItemRepository transactionItemRepository, ProductService productService) {
        this.transactionItemRepository = transactionItemRepository;
        this.productService = productService;
    }


    public List<TransactionItem> findAll() {
        return transactionItemRepository.findAll();
    }

    public TransactionItem findOne(Long id) {
        return transactionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction-item", "id", id));
    }

    public TransactionItem create(Long productId, Integer quantity) {
        var product = productService.findOne(productId);
        var transactionItem = new TransactionItem(product, quantity);
        var newTransactionItem = transactionItemRepository.save(transactionItem);

        log.info("Add transaction-item: {}", newTransactionItem);
        return newTransactionItem;
    }

    @Transactional
    public TransactionItem update(Long id, Long productId, Integer quantity) {
        var transaction = transactionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction-item", "id", id));

        if (productId != null && !productId.equals(transaction.getProduct().getId()))
            transaction.setProduct(productService.findOne(productId));
        if (quantity != null && !quantity.equals(transaction.getQuantity()))
            transaction.setQuantity(quantity);

        return transaction;
    }

    public TransactionItem delete(Long id) {
        var transactionItem = transactionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction-item", "id", id));
        transactionItemRepository.delete(transactionItem);
        return transactionItem;
    }
}
