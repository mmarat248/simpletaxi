package com.example.demo.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthPayload {
    private UUID userId;
    private String username;
}
