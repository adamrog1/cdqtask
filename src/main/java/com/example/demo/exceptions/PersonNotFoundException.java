package com.example.demo.exceptions;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(Long id) {
        super("Employee with ID " + id + " not found.");
    }
}
