package com.example.pharmacy.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Сущность категория")
public class CategoryDTO {

    @Schema(description = "ID категории", example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Название категории", example = "Гигиена полости рта")
    private String title;
}
