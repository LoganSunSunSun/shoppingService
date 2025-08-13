package org.example.shoppingproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RecentItemDto {
    private Long productId;
    private String productName;
    private LocalDateTime lastBoughtAt;
    private Long timesBought;
}