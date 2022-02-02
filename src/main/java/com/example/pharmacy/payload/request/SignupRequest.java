package com.example.pharmacy.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность пользователь")
public class SignupRequest {

    @JsonProperty("username")
    @Schema(description = "Никнейм", example = "User123")
    private String username;

    @JsonProperty("firstname")
    @Schema(description = "Имя пользователя", example = "Alexander")
    private String firstname;

    @JsonProperty("lastname")
    @Schema(description = "Фамилия пользователя", example = "Ivanov")
    private String lastname;

    @JsonProperty("password")
    @Schema(description = "Пароль", example = "password123")
    private String password;

    @JsonProperty("matchingPassword")
    @Schema(description = "Подтверждение пароля", example = "password123")
    private String matchingPassword;

    @JsonProperty("email")
    @Schema(description = "Почта пользователя", example = "spicin@gmail.com")
    private String email;

}
