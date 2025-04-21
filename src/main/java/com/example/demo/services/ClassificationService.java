package com.example.demo.services;

import com.example.demo.constants.TaskResult;
import com.example.demo.constants.TaskStatus;
import com.example.demo.models.Person;
import com.example.demo.models.Task;
import com.example.demo.repositories.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassificationService {
    private static final Logger logger = LoggerFactory.getLogger(ClassificationService.class);
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    @Async
    protected void processTask(Person current, Optional<Person> previous, Task task) throws JsonProcessingException {
        Map<String, TaskResult> classifications = new LinkedHashMap<>();

        classifications.put("name", classify(
                previous.map(Person::getName).orElse(null),
                current.getName()));
        updateTaskProgress(task, 25);

        classifications.put("surname", classify(
                previous.map(Person::getSurname).orElse(null),
                current.getSurname()));
        updateTaskProgress(task, 50);

        classifications.put("birthDate", classify(
                String.valueOf(previous.map(Person::getBirthDate).orElse(null)),
                current.getBirthDate() != null ? String.valueOf(current.getBirthDate()) : null));
        updateTaskProgress(task, 75);

        classifications.put("company", classify(
                previous.map(Person::getCompany).orElse(null),
                current.getCompany()));
        updateTaskProgress(task, 100);

        task.setResult(objectMapper.writeValueAsString(classifications));
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);

        logger.info("Task processed with result: {}", classifications);
    }

    private TaskResult classify(String oldVal, String newVal) {
        if (oldVal == null && newVal != null) return TaskResult.ADDED;
        if (oldVal != null && newVal == null) return TaskResult.DELETED;
        if (oldVal == null && newVal == null) return TaskResult.HIGH;

        double sim = 1.0 - simpleDissimilarity(oldVal, newVal);
        if (sim > 0.9) return TaskResult.HIGH;
        else if (sim >= 0.4) return TaskResult.MEDIUM;
        else return TaskResult.LOW;
    }

    private double simpleDissimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 1.0;
        int len1 = s1.length(), len2 = s2.length();
        int maxLen = Math.max(len1, len2);
        if (maxLen == 0) return 0.0;

        int bestDiff = Integer.MAX_VALUE;

        for (int shift = -len2; shift <= len1; shift++) {
            int overlapStart = Math.max(0, shift);
            int overlapEnd   = Math.min(len1, shift + len2);

            int diffCount = 0;
            for (int i = overlapStart; i < overlapEnd; i++) {
                if (s1.charAt(i) != s2.charAt(i - shift)) {
                    diffCount++;
                }
            }

            int totalOverlap = overlapEnd - overlapStart;
            int leftover = maxLen - totalOverlap;

            bestDiff = Math.min(bestDiff, diffCount + leftover);
        }

        return (double) bestDiff / maxLen;
    }


    private void updateTaskProgress(Task task, int progress) {
        doLongRunningTask(task.getTaskId());
        task.setProgress(progress);
        taskRepository.save(task);
    }

    private void doLongRunningTask(String taskId) {
        try {
            logger.info("Started additional task to longer the task processing");
            Thread.sleep(3000); // simulate work
            logger.info("Finished long running task: {}", taskId);
        } catch (InterruptedException e) {
            logger.error("Error in long task", e);
        }
    }
}
