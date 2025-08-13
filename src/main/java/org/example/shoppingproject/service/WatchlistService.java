package org.example.shoppingproject.service;


import org.example.shoppingproject.dao.ProductRepository;
import org.example.shoppingproject.dao.UserRepository;
import org.example.shoppingproject.dao.WatchlistRepository;
import org.example.shoppingproject.domain.Product;
import org.example.shoppingproject.domain.User;
import org.example.shoppingproject.domain.Watchlist;
import org.example.shoppingproject.exception.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Transactional
public class WatchlistService {

    private final WatchlistRepository repository;
    private final UserRepository userRepository; // also using Hibernate Session
    private final ProductRepository productRepository;

    public WatchlistService(WatchlistRepository repository,
                            UserRepository userRepository,
                            ProductRepository productRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void addToWatchlist(Long userId, Long productId) {
        if (repository.existsByUserAndProduct(userId, productId)) {
            throw new IllegalStateException("Product already in watchlist");
        }
        User user = userRepository.findById(userId);

        Product product = productRepository.findById(productId);
//                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        repository.save(new Watchlist(user, product));
    }

    public void removeFromWatchlist(Long userId, Long productId) {
        repository.deleteByUserAndProduct(userId, productId);
    }

    public List<Product> getWatchlist(Long userId) {
        List<Watchlist> list = repository.findInStockByUser(userId);
        return list.stream().map(Watchlist::getProduct).collect(Collectors.toList());
    }
}
