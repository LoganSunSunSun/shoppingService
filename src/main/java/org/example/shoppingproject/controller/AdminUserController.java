package org.example.shoppingproject.controller;

import org.example.shoppingproject.domain.Order;
import org.example.shoppingproject.domain.Product;
import org.example.shoppingproject.dto.OrderDto;
import org.example.shoppingproject.dto.ProductDto;
import org.example.shoppingproject.service.AdminService;
import org.example.shoppingproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // only admins can call this
public class AdminUserController {

    private final UserService userService;
    private final AdminService adminService;

    public AdminUserController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @PatchMapping("/users/{userId}/promote")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long userId) {
        userService.promoteToAdmin(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User promoted to admin successfully");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/orders")
    public List<OrderDto> getOrders(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        return adminService.getOrdersDto(page, size);
    }

    @GetMapping("/products")
    public List<ProductDto> getProducts() {
        return adminService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) throws Exception {
        return adminService.getProductById(id);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product p = adminService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @PatchMapping("/products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) throws Exception {
        return adminService.updateProduct(id, product);
    }

    @PatchMapping("/orders/{id}/complete")
    public Order completeOrder(@PathVariable Long id) throws Exception {
        return adminService.completeOrder(id);
    }

    @PatchMapping("/orders/{id}/cancel")
    public Order cancelOrder(@PathVariable Long id) throws Exception {
        return adminService.cancelOrder(id);
    }

    @GetMapping("/summary/total-sold")
    public Long totalSold() {
        return adminService.getTotalSold();
    }

    @GetMapping("/summary/top-3-products")
    public List<Object[]> top3Products() {
        return adminService.getTop3Products();
    }

    @GetMapping("/summary/top-profit")
    public Object[] topProfit() {
        return adminService.getTopProfitProduct();
    }
}
