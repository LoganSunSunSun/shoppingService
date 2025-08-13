package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {
    List<Product> findInStockProducts();
    List<Product> findPaginated(int pageNumber, int pageSize);
    Product findById(Long id);
    void save(Product product);
    void update(Product product);
}
