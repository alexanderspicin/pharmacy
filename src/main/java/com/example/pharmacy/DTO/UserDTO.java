package com.example.pharmacy.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("Id")
    @Schema(description = "Id", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

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
    @Schema(description = "Пароль", example = "password123", required = false)
    private String password;

    @JsonProperty("email")
    @Schema(description = "Почта пользователя", example = "spicin@gmail.com")
    private String email;

}
