package org.example.shoppingproject.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long productId;  // snapshot of product id at purchase

    @Column(nullable = false, length = 150)
    private String productName;  // snapshot

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal wholesaleAtPurchase;  // snapshot

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal retailAtPurchase;  // snapshot

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;  // retailAtPurchase * quantity

    // Getters and setters
}

