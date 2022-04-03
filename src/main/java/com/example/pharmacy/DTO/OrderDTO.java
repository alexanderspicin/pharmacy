package com.example.pharmacy.DTO;

import com.example.pharmacy.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Сущность заказ")
public class OrderDTO {

    private Long id;

    private LocalDateTime createTime;

    private Status status;

    private int amountProducts;

    private Double sum;

    private List<OrderDetailDTO> orderDetails = new ArrayList<>();

    public void aggregate(){
        this.amountProducts = orderDetails.size();
        this.sum = orderDetails.stream()
                .map(OrderDetailDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
