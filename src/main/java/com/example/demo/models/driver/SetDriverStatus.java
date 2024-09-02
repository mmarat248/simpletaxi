package com.example.demo.models.driver;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SetDriverStatus {
    @NotNull
    private Driver.Status status;
}
