package org.example.shoppingproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.shoppingproject.domain.Product;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String description;
    private double retailPrice;

    // For user view, no quantity or wholesale price
    public static ProductDto fromEntity(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setDescription(product.getDescription());
        dto.setRetailPrice(product.getRetailPrice().doubleValue());
        return dto;
    }
}
