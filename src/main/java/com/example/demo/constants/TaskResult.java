package com.example.demo.constants;

public enum TaskResult {
    STARTED("Started", "Task Just Started"),
    ADDED("Added", "previous value was not present"),
    LOW("Low", "similarity < 0.4"),
    MEDIUM("Medium", "similarity in the range [0.4, 0.9]"),
    HIGH("High", "similarity > 0.9"),
    DELETED("Deleted", "The task has been finished");

    private final String displayName;
    private final String description;

    TaskResult(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}
