package com.example.demo.constants;

public enum TaskStatus {

    IN_PROGRESS("In Progress", "The task is currently being worked on"),
    COMPLETED("Completed", "The task has been finished");

    private final String displayName;
    private final String description;

    TaskStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}
