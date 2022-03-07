package com.example.pharmacy.DTO;

import com.example.pharmacy.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Сущность продукт")
public class ProductDTO {

    @JsonProperty("id")
    @Schema(description = "ID продукта", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @JsonProperty("productName")
    @Schema(description = "Название продукта", example = "ORAL-B ЗУБНАЯ НИТЬ ESSENTIAL FLOSS НEВОЩЕНАЯ 50М")
    private String productName;

    @JsonProperty("productDescription")
    @Schema(description = "Описание продукта", example = "ORAL-B ЗУБНАЯ НИТЬ ESSENTIAL FLOSS НЕВОЩЕНАЯ - невощеное волокно обеспечивает более тщательное очищение межзубных промежутков за счет трения.")
    private String productDescription;

    @JsonProperty("price")
    @Schema(description = "Цена продукта", example = "1500.50")
    private Double price;

    @JsonProperty("manifacturer")
    @Schema(description = "Производитель", example = "Procter & Gamble (Manufacturing) Ireland Ltd.")
    private String manifacturer;

    @JsonProperty("composition")
    @Schema(description = "Состав", example = "Nylon, Pebax")
    private String composition;

    @JsonProperty("categories")
    @Schema(description = "Список категорий")
    private List<Category> categories;

    @JsonProperty("indications")
    @Schema(description = "Показания", example = "Гигиена полости рта - облегчает удаление налета")
    private String indications;

    @JsonProperty("image")
    @Schema(description = "Изображение в виде S3 KEY")
    private String imageLink;
}
