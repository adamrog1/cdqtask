package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {

    private String personId;
    private String taskId;
    private String message;

    public PersonResponse(String message) {
        this.message = message;
    }
}