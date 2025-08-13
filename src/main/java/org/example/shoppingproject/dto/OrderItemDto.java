package org.example.shoppingproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.shoppingproject.domain.OrderItem;

@Getter
@Setter
public class OrderItemDto {
    private Long productId;
    private String productDescription;
    private int quantity;
    private double priceAtPurchase; // Use retail price snapshot

    public static OrderItemDto fromEntity(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setProductId(item.getProductId());
        dto.setProductDescription(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtPurchase(item.getRetailAtPurchase().doubleValue());
        return dto;
    }
}