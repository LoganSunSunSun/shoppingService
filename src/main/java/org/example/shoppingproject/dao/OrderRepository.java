package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.Order;
import org.example.shoppingproject.dto.RecentItemDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {
    List<Order> findOrdersByUserId(Long userId);
    Order findByIdAndUserId(Long orderId, Long userId);
    Optional<Order> findById(Long orderId); // new method
    void save(Order order);
    void update(Order order);
    List<RecentItemDto> findRecentItemsByUser(Long userId, int limit);
}