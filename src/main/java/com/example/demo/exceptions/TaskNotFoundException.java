package com.example.demo.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String id) {
        super("Task with ID " + id + " not found.");
    }
}

