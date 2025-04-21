package com.example.demo.controllers;

import com.example.demo.dto.TaskDTO;
import com.example.demo.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable("taskId") String personId) {
        TaskDTO task = taskService.getTask(personId);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TaskDTO>> getTasks() {
        List<TaskDTO> tasks = taskService.getAll();
        if (!tasks.isEmpty()) {
            return ResponseEntity.ok(tasks);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
