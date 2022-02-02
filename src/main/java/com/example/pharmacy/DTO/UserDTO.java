package com.example.pharmacy.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private String role;

    private String createTime;

}
