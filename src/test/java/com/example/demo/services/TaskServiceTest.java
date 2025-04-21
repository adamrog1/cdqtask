package com.example.demo.services;

import com.example.demo.dto.TaskDTO;
import com.example.demo.constants.TaskStatus;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.models.Person;
import com.example.demo.models.Task;
import com.example.demo.repositories.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private ClassificationService classificationService;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        classificationService = mock(ClassificationService.class);
        taskService = new TaskService(taskRepository, classificationService);
    }

    @Test
    void startClassificationTask_successfulCreatesAndReturnsId() throws JsonProcessingException {
        // Arrange
        Person current = new Person();
        current.setName("John");
        current.setSurname("Doe");
        current.setBirthDate(LocalDate.of(2000, 1, 1));
        current.setCompany("Acme");
        Optional<Person> previous = Optional.empty();

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        when(taskRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        String returnedId = taskService.startClassificationTask(current, previous);

        // Assert
        Task saved = captor.getValue();
        assertNotNull(saved.getTaskId());
        assertEquals(returnedId, saved.getTaskId());
        assertEquals(TaskStatus.IN_PROGRESS, saved.getStatus());
        // Verify
        verify(classificationService).processTask(current, previous, saved);
    }

    @Test
    void startClassificationTask_whenExceptionReturnsErrorMessage() {
        // Arrange
        Person current = new Person();
        Optional<Person> previous = Optional.empty();
        when(taskRepository.save(any())).thenThrow(new RuntimeException("DB down"));

        // Act
        String result = taskService.startClassificationTask(current, previous);

        // Assert
        assertEquals("Didn't start task", result);
    }

    @Test
    void getTask_existingTask_returnsDto() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, TaskStatus.COMPLETED);
        task.setResult("{\"key\":\"value\"}");
        when(taskRepository.findByTaskId(id)).thenReturn(Optional.of(task));

        // Act
        TaskDTO dto = taskService.getTask(id);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(TaskStatus.COMPLETED.name(), dto.getStatus());
        JsonNode node = dto.getResult();
        assertEquals("value", node.get("key").asText());
    }

    @Test
    void getTask_unknownTask_throwsException() {
        // Arrange
        when(taskRepository.findByTaskId("missing")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask("missing"));
    }

    @Test
    void getAll_returnsMappedList() {
        // Arrange
        Task t1 = new Task("id1", TaskStatus.IN_PROGRESS);
        Task t2 = new Task("id2", TaskStatus.IN_PROGRESS);
        when(taskRepository.findAll()).thenReturn(List.of(t1, t2));

        // Act
        List<TaskDTO> list = taskService.getAll();

        // Assert
        assertEquals(2, list.size());
        TaskDTO dto1 = list.get(0);
        assertEquals("id1", dto1.getId());
        TaskDTO dto2 = list.get(1);
        assertEquals("id2", dto2.getId());
    }
}
