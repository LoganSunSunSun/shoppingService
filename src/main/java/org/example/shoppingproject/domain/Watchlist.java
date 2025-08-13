package org.example.shoppingproject.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "watchlists",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
//@AllArgsConstructor
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @CreationTimestamp
    @Column(name="added_at")
    private LocalDateTime addedAt;

    // Hibernate requires a no-args constructor
    public Watchlist() {}

    // Constructor for user + product
    public Watchlist(User user, Product product) {
        this.user = user;
        this.product = product;
    }
    // Getters and setters
}

