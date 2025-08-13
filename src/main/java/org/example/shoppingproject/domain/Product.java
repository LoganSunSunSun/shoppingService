package org.example.shoppingproject.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2, name="wholesale_price")
    private BigDecimal wholesalePrice;

    @Column(nullable = false, precision = 10, scale = 2, name="retail_price")
    private BigDecimal retailPrice;

    @Column(nullable = false)
    private int quantity;  // current stock

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", insertable = false, updatable = true)
    private LocalDateTime updatedAt;

    // Getters and setters
}
