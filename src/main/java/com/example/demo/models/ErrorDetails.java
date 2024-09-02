package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public record ErrorDetails(
    String message,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> errors
) {
    public ErrorDetails(String message) {
        this(message, List.of());
    }
}