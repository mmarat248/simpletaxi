package com.example.demo.models.auth;


import jakarta.validation.constraints.NotNull;


public record SignInRequest (
    @NotNull String username,
    @NotNull char[] password
) {}
