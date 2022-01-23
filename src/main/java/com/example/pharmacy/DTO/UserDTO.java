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
@Schema(description = "Сущность пользователь")
public class UserDTO {

    @Schema(description = "Никнейм", example = "User123")
    private String username;
    @Schema(description = "Имя пользователя", example = "Alexander")
    private String firstname;
    @Schema(description = "Фамилия пользователя", example = "Ivanov")
    private String lastname;
    @Schema(description = "Пароль", example = "password123")
    private String password;
    @Schema(description = "Подтверждение пароля", example = "password123")
    private String matchingPassword;
    @Schema(description = "Почта пользователя", example = "spicin@gmail.com")
    private String email;

}
