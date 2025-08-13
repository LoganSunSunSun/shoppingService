package org.example.shoppingproject.service;

import lombok.RequiredArgsConstructor;
import org.example.shoppingproject.dao.OrderItemRepository;
import org.example.shoppingproject.dao.OrderRepository;
import org.example.shoppingproject.dao.ProductRepository;
import org.example.shoppingproject.dao.UserRepository;
import org.example.shoppingproject.domain.Order;
import org.example.shoppingproject.domain.OrderItem;
import org.example.shoppingproject.domain.Product;
import org.example.shoppingproject.domain.User;
import org.example.shoppingproject.dto.PlaceOrderRequest;
import org.example.shoppingproject.dto.RecentItemDto;
import org.example.shoppingproject.exception.AccessDeniedException;
import org.example.shoppingproject.exception.NotEnoughInventoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final AuthService authService; // To get current user

    @Transactional
    public Order placeOrder(PlaceOrderRequest request) throws NotEnoughInventoryException, AccessDeniedException {
        User user = authService.getCurrentUser();

        // 1. Validate stock for each product
        Map<Long, Product> productMap = new HashMap<>();
        for (PlaceOrderRequest.OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId());
            if (product == null) throw new EntityNotFoundException("Product not found: " + itemReq.getProductId());

            if (itemReq.getQuantity() > product.getQuantity()) {
                throw new NotEnoughInventoryException("Not enough stock for product " + product.getName());
            }
            productMap.put(product.getId(), product);
        }

        // 2. Deduct stock atomically & save order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PROCESSING);
        order.setPlacedAt(LocalDateTime.now());
        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (PlaceOrderRequest.OrderItemRequest itemReq : request.getItems()) {
            Product product = productMap.get(itemReq.getProductId());

            // Deduct stock
            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setWholesaleAtPurchase(product.getWholesalePrice());
            orderItem.setRetailAtPurchase(product.getRetailPrice());
            orderItem.setLineTotal(product.getRetailPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        orderItemRepository.saveAll(orderItems);

        return order;
    }

    @Transactional
    public void cancelOrder(Long orderId) throws AccessDeniedException {
        User user = authService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Cannot cancel others' orders");
        }

        if (order.getStatus() == Order.Status.COMPLETED) {
            throw new IllegalStateException("Completed order cannot be canceled");
        }

        if (order.getStatus() == Order.Status.CANCELED) {
            return; // Already canceled, no-op
        }

        // Set status to canceled
        order.setStatus(Order.Status.CANCELED);

        // Increment product stock back
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId());
//                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + item.getProductId()));
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);
    }

    public List<Order> listUserOrders() throws AccessDeniedException {
        User user = authService.getCurrentUser();
        return orderRepository.findOrdersByUserId(user.getId());
    }

    public Order getOrderDetail(Long orderId) throws AccessDeniedException {
        User user = authService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }
        return order;
    }

    @Transactional()
    public List<RecentItemDto> getRecentItems(int limit) throws AccessDeniedException {
        User user = authService.getCurrentUser();
        return orderRepository.findRecentItemsByUser(user.getId(), limit);
    }
}
