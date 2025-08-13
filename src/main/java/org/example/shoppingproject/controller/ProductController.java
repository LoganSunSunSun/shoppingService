package org.example.shoppingproject.controller;

import org.example.shoppingproject.domain.Product;
import org.example.shoppingproject.dto.ProductDto;
import org.example.shoppingproject.exception.ProductNotFoundException;
import org.example.shoppingproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/products")
@PreAuthorize("hasRole('USER')")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET /api/products?&page=0&size=20
    @GetMapping
    public ResponseEntity<List<ProductDto>> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(productService.getPaginatedProducts(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductDetail(@PathVariable Long id) throws ProductNotFoundException {
        Product product = productService.getProductDetail(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProductDto.fromEntity(product));
    }
}

