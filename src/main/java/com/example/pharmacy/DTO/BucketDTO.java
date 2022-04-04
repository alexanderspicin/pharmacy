package com.example.pharmacy.DTO;


import com.example.pharmacy.entity.Promocode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Сущность корзина")
public class BucketDTO {
    private int amountProducts;

    private Double sum;

    private int saleSum;

    private Promocode promocode;

    private Double totalSum;

    private List<BucketDetailDTO> bucketDetails = new ArrayList<>();

    public void aggregate(){
        this.amountProducts = bucketDetails.size();
        this.sum = bucketDetails.stream()
                .map(BucketDetailDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
