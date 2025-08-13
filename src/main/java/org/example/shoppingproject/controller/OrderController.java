package org.example.shoppingproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.shoppingproject.domain.Order;
import org.example.shoppingproject.dto.OrderDto;
import org.example.shoppingproject.dto.PlaceOrderRequest;
import org.example.shoppingproject.exception.AccessDeniedException;
import org.example.shoppingproject.exception.NotEnoughInventoryException;
import org.example.shoppingproject.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderService orderService;

    // POST /api/orders — place an order
    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderRequest request) throws NotEnoughInventoryException, AccessDeniedException {
        Order order = orderService.placeOrder(request);
        return ResponseEntity.ok(OrderDto.fromEntity(order));
    }

    // PATCH /api/orders/{id}/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) throws AccessDeniedException {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/orders
    @GetMapping
    public ResponseEntity<List<OrderDto>> listUserOrders() throws AccessDeniedException {
        List<Order> orders = orderService.listUserOrders();
        List<OrderDto> dtoList = orders.stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // GET /api/orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderDetail(@PathVariable Long id) throws AccessDeniedException {
        Order order = orderService.getOrderDetail(id);
        return ResponseEntity.ok(OrderDto.fromEntity(order));
    }

    // GET /api/orders/recent?limit=x
    @GetMapping("/recent")
    public ResponseEntity<List<?>> getRecentItems(@RequestParam(defaultValue = "5") int limit) throws AccessDeniedException {
        return ResponseEntity.ok(orderService.getRecentItems(limit));
    }
}