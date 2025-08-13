package org.example.shoppingproject.service;


import org.example.shoppingproject.dao.WatchlistRepository;
import org.example.shoppingproject.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    public List<Product> getWatchlist(Long userId) {
        return watchlistRepository.findWatchlistProductsByUserId(userId);
    }

    public void addToWatchlist(Long userId, Long productId) {
        watchlistRepository.addProductToWatchlist(userId, productId);
    }

    public void removeFromWatchlist(Long userId, Long productId) {
        watchlistRepository.removeProductFromWatchlist(userId, productId);
    }
}