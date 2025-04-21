package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    private String company;
}
