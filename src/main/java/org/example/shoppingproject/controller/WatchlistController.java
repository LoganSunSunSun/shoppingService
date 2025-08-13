package org.example.shoppingproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.shoppingproject.domain.Product;
import org.example.shoppingproject.domain.User;
import org.example.shoppingproject.dto.ProductDto;
import org.example.shoppingproject.exception.AccessDeniedException;
import org.example.shoppingproject.service.AuthService;
import org.example.shoppingproject.service.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class WatchlistController {

    private final WatchlistService service;
//    private final JwtUtils jwtUtils; // assume you extract userId from JWT
    private final AuthService authService;

    // Add product
    @PostMapping("/{productId}")
    public ResponseEntity<String> add(@PathVariable Long productId) throws AccessDeniedException {
//        Long userId = jwtUtils.getUserIdFromToken(token);
        User user = authService.getCurrentUser();
        service.addToWatchlist(user.getId(), productId);
        return ResponseEntity.ok("Product added to watchlist");
    }

    // Remove product
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> remove(@PathVariable Long productId) throws AccessDeniedException {

        User user = authService.getCurrentUser();
        service.removeFromWatchlist(user.getId(), productId);
        return ResponseEntity.ok("Product removed from watchlist");
    }

    // Get all in-stock products
    @GetMapping
    public ResponseEntity<List<ProductDto>> list(@RequestHeader("Authorization") String token) throws AccessDeniedException {
        User user = authService.getCurrentUser();
        List<Product> products = service.getWatchlist(user.getId());
        List<ProductDto> dtos = products.stream()
                .map(p -> new ProductDto(p.getId(), p.getDescription(), p.getRetailPrice().doubleValue()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}

