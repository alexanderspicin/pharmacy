package com.example.pharmacy.DTO;

import com.example.pharmacy.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {

    private String productName;

    private Long productId;

    private Double price;

    private int amount;

    private Double sum;

    public OrderDetailDTO(Product product) {
        this.productName = product.getProductName();
        this.productId = product.getId();
        this.price = Double.valueOf(product.getPrice().toString());
        this.amount = 1;
        this.sum = Double.valueOf(product.getPrice().toString());
    }
}
