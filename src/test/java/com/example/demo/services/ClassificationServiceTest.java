package com.example.demo.services;

import com.example.demo.constants.TaskResult;
import com.example.demo.repositories.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ClassificationServiceTest {

    private ClassificationService service;

    @BeforeEach
    void setUp() {
        TaskRepository taskRepository = mock(TaskRepository.class);
        ObjectMapper objectMapper = new ObjectMapper();
        service = new ClassificationService(taskRepository, objectMapper);
    }

    @Test
    void classify_nullPrev_nonNullCurrent_returnsAdded() {
        TaskResult result = ReflectionTestUtils.invokeMethod(
                service, "classify", null, "John");
        assertEquals(TaskResult.ADDED, result);
    }

    @Test
    void classify_nonNullPrev_nullCurrent_returnsDeleted() {
        TaskResult result = ReflectionTestUtils.invokeMethod(
                service, "classify", "Jane", null);
        assertEquals(TaskResult.DELETED, result);
    }

    @Test
    void classify_bothNull_returnsHigh() {
        TaskResult result = ReflectionTestUtils.invokeMethod(
                service, "classify", null, null);
        assertEquals(TaskResult.HIGH, result);
    }

    @Test
    void classify_identical_returnsHigh() {
        TaskResult result = ReflectionTestUtils.invokeMethod(
                service, "classify", "ABC", "ABC");
        assertEquals(TaskResult.HIGH, result);
    }

    @Test
    void classify_lowSimilarity_returnsLow() {
        // difference 3 of 4 => dissimilarity .75 => similarity .25 < .4
        TaskResult result = ReflectionTestUtils.invokeMethod(
                service, "classify", "ABCDEFGH", "TDD");
        assertEquals(TaskResult.LOW, result);
    }

    @Test
    void classify_mediumSimilarity_returnsMedium() {
        TaskResult result = ReflectionTestUtils.invokeMethod(
                service, "classify", "ABCABC", "ABC");
        assertEquals(TaskResult.MEDIUM, result);
    }

    @Test
    void classify_mediumSimilarity_returnsMedium2() {
        TaskResult result = ReflectionTestUtils.invokeMethod(
                service, "classify", "ABCD", "BCD");
        assertEquals(TaskResult.MEDIUM, result);
    }

    @Test
    void testSimpleDissimilarity_variousPairs() {
        double d1 = ReflectionTestUtils.invokeMethod(
                service, "simpleDissimilarity", "ABCD", "BCD");
        assertEquals(1.0 / 4, d1, 1e-6);

        double d2 = ReflectionTestUtils.invokeMethod(
                service, "simpleDissimilarity", "", "");
        assertEquals(0.0, d2, 1e-6);

        double d3 = ReflectionTestUtils.invokeMethod(
                service, "simpleDissimilarity", null, "XYZ");
        assertEquals(1.0, d3, 1e-6);

        double d4 = ReflectionTestUtils.invokeMethod(
                service, "simpleDissimilarity", "ABCDEFGH", "TDD");
        assertEquals(7.0 / 8, d4, 1e-6);
    }
}
