package org.example.shoppingproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.shoppingproject.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private String status;
    private LocalDateTime placedAt;
    private List<OrderItemDto> items;

    public static OrderDto fromEntity(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setPlacedAt(order.getPlacedAt());
        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(OrderItemDto::fromEntity)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);
        return dto;
    }

}