package com.nick.coffeeshop.service;


import com.nick.coffeeshop.enums.Role;
import com.nick.coffeeshop.exception.ResourceNotFoundException;
import com.nick.coffeeshop.model.User;
import com.nick.coffeeshop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public User create(String username, String password, Role role) {
        var user = new User(username, password, role);
        return userRepository.save(user);
    }

//    @Transactional
//    public User update(Long id, String name, ProductSize productSize, ProductType productType) {
//        var product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
//
//        if (name != null && !name.equals(product.getName()))
//            product.setName(name);
//        if (productSize != null && productSize != product.getProductSize())
//            product.setProductSize(productSize);
//        if (productType != null && productType != product.getProductType())
//            product.setProductType(productType);
//
//        return product;
//    }
//
//    public User delete(Long id) {
//        var product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
//        productRepository.delete(product);
//        return product;
//    }
}
