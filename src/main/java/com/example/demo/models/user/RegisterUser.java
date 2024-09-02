package com.example.demo.models.user;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class RegisterUser {

    @NotEmpty
    private String username;

    @NotEmpty
    private char[] password;

}
