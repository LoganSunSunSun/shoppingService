package org.example.shoppingproject.service;

import org.example.shoppingproject.dao.ProductRepository;
import org.example.shoppingproject.dao.ProductRepositoryImpl;
import org.example.shoppingproject.domain.Product;
import org.example.shoppingproject.dto.ProductDto;
import org.example.shoppingproject.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductRepositoryImpl productRepositoryImpl;

    public List<Product> getInStockProducts() {
        return productRepository.findInStockProducts();
    }

    public List<ProductDto> getPaginatedProducts(int pageNumber, int pageSize) {
        return productRepositoryImpl.findPaginated(pageNumber, pageSize).stream().map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Product getProductDetail(Long productId) throws ProductNotFoundException {
        Product p = productRepository.findById(productId);
        if (p == null || p.getQuantity() <= 0) {
            throw new ProductNotFoundException("Product not found or out of stock");
        }
        return p;
    }
}
