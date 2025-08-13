package org.example.shoppingproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore // prevent infinite recursion
    private Order order;

    @Column(name="product_id", nullable = false)
    private Long productId;  // snapshot of product id at purchase

    @Column(name="product_name", nullable = false, length = 150)
    private String productName;  // snapshot

    @Column(name="wholesale_at_purchase", nullable = false, precision = 10, scale = 2)
    private BigDecimal wholesaleAtPurchase;  // snapshot

    @Column(name="retail_at_purchase", nullable = false, precision = 10, scale = 2)
    private BigDecimal retailAtPurchase;  // snapshot

    @Column(nullable = false)
    private int quantity;

    @Column(name = "line_total", insertable = false, updatable = false)
    private BigDecimal lineTotal;  // retailAtPurchase * quantity

    // Getters and setters
}

