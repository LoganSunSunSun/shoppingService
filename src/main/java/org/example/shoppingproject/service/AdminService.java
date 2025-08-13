package org.example.shoppingproject.service;


import org.example.shoppingproject.dao.AdminRepository;
import org.example.shoppingproject.domain.Order;
import org.example.shoppingproject.domain.Product;
import org.example.shoppingproject.dto.OrderDto;
import org.example.shoppingproject.dto.OrderItemDto;
import org.example.shoppingproject.dto.ProductDto;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final SessionFactory sessionFactory;

    public AdminService(AdminRepository adminRepository, SessionFactory sessionFactory) {
        this.adminRepository = adminRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Order> getOrders(int page, int size) {
        return adminRepository.findOrdersForAdmin(page, size);
    }

    @Transactional
    public List<OrderDto> getOrdersDto(int page, int size) {
        List<Order> orders = adminRepository.findOrdersForAdmin(page, size);

        return orders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setStatus(order.getStatus().name());
            dto.setPlacedAt(order.getPlacedAt());
            dto.setItems(order.getOrderItems().stream()
                    .map(OrderItemDto::fromEntity)
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ProductDto getProductDto(Long id) {
        Product p = adminRepository.getProductById(id);
        return ProductDto.fromEntity(p);
    }

    @Transactional
    public List<ProductDto> getAllProducts() {
        return adminRepository.getAllProducts().stream().map(ProductDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public Product getProductById(Long id) throws Exception {
        Product p = adminRepository.getProductById(id);
        if (p == null) throw new Exception("Product not found");
        return p;
    }

    @Transactional
    public Product addProduct(Product product) {
        return adminRepository.saveOrUpdateProduct(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product updated) throws Exception {
        Product existing = adminRepository.getProductById(id);
        if (existing == null) throw new Exception("Product not found");
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setWholesalePrice(updated.getWholesalePrice());
        existing.setRetailPrice(updated.getRetailPrice());
        existing.setQuantity(updated.getQuantity());
        return adminRepository.saveOrUpdateProduct(existing);
    }

    @Transactional
    public Order completeOrder(Long orderId) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Order order = session.get(Order.class, orderId, LockMode.PESSIMISTIC_WRITE);
        if (order == null) throw new Exception("Order not found");
        switch (order.getStatus()) {
            case COMPLETED:
                throw new IllegalStateException("Order already completed");
            case CANCELED:
                throw new IllegalStateException("Cannot complete a canceled order");
            default:
                order.setStatus(Order.Status.COMPLETED);
                return adminRepository.saveOrUpdateOrder(order);
        }
    }

    @Transactional
    public Order cancelOrder(Long orderId) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Order order = session.get(Order.class, orderId, LockMode.PESSIMISTIC_WRITE);
        if (order == null) throw new Exception("Order not found");
        switch (order.getStatus()) {
            case COMPLETED:
                throw new IllegalStateException("Cannot cancel a completed order");
            case CANCELED:
                return order; // already canceled
            default:
                order.setStatus(Order.Status.CANCELED);
                // increment stock
                order.getOrderItems().forEach(item -> {
                    Product p = session.get(Product.class, item.getProductId(), LockMode.PESSIMISTIC_WRITE);
                    p.setQuantity(p.getQuantity() + item.getQuantity());
                });
                return adminRepository.saveOrUpdateOrder(order);
        }
    }

    @Transactional
    public Long getTotalSold() {
        return adminRepository.totalSoldItems();
    }

    @Transactional
    public List<Object[]> getTop3Products() {
        return adminRepository.top3ProductsByUnitsSold();
    }

    @Transactional
    public Object[] getTopProfitProduct() {
        return adminRepository.topProfitProduct();
    }
}