package com.example.demo.models.driver;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class DriverListFilter {
    @Min(1)
    @Max(100)
    public int size = 20;

    public Driver.Status status;
}
