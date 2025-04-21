package com.example.demo.models;

import com.example.demo.constants.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskId;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private Integer progress;
    private String result;

    public Task(String uuid, TaskStatus status) {
        this.taskId = uuid;
        this.status = status;
        this.progress = 0;

    }

    public Task() {

    }
}
