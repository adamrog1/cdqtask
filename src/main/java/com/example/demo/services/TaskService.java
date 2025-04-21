package com.example.demo.services;

import com.example.demo.dto.TaskDTO;
import com.example.demo.constants.TaskStatus;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.mappers.TaskMapper;
import com.example.demo.models.Person;
import com.example.demo.models.Task;
import com.example.demo.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final ClassificationService classificationService;

    public String startClassificationTask(Person current, Optional<Person> previous) {
        try {
            String taskId = UUID.randomUUID().toString();
            Task task = taskRepository.save(new Task(taskId, TaskStatus.IN_PROGRESS));
            logger.info("Started the task " + taskId);
            classificationService.processTask(current, previous, task);
            return taskId;
        }
        catch(Exception e ) {
            logger.error("There was exception in task " + e.getMessage());
            return "Didn't start task";
        }
    }

    @Cacheable(value = "taskCache", key = "#taskId")
    public TaskDTO getTask(String taskId) {
        return TaskMapper.toDto(taskRepository.findByTaskId(taskId).orElseThrow(() -> new TaskNotFoundException(taskId)));
    }

    @Cacheable("tasks")
    public List<TaskDTO> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }
}
