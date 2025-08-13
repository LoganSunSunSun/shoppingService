package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository {
    List<Product> findWatchlistProductsByUserId(Long userId);
    void addProductToWatchlist(Long userId, Long productId);
    void removeProductFromWatchlist(Long userId, Long productId);
}
