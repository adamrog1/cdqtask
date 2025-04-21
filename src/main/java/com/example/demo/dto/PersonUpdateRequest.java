package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonUpdateRequest {

    private String name;
    private String surname;
    private LocalDate birthDate;
    private String company;
}
