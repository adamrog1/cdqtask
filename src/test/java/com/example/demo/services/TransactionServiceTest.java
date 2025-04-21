package com.example.demo.services;

import com.example.demo.dto.PersonDTO;
import com.example.demo.dto.PersonResponse;
import com.example.demo.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private PersonService personService;
    private TaskService taskService;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        personService = mock(PersonService.class);
        taskService = mock(TaskService.class);
        transactionService = new TransactionService(personService, taskService);
    }

    @Test
    void createPersonAndStartTask_success() {
        // Given
        PersonDTO dto = new PersonDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(LocalDate.of(1980, 5, 20));
        dto.setCompany("Acme");

        Person createdPerson = new Person();
        createdPerson.setId(100L);
        when(personService.createPerson(dto)).thenReturn(createdPerson);

        Person previousPerson = new Person();
        previousPerson.setId(50L);
        when(personService.getPreviousPerson(100L)).thenReturn(Optional.of(previousPerson));

        when(taskService.startClassificationTask(createdPerson, Optional.of(previousPerson)))
                .thenReturn("task-abc");

        // When
        PersonResponse response = transactionService.createPersonAndStartTask(dto);

        // Then
        assertNotNull(response);
        assertEquals("100", response.getPersonId());
        assertEquals("task-abc", response.getTaskId());
        assertEquals("Person Created", response.getMessage());

        // Verify interactions
        verify(personService).createPerson(dto);
        verify(personService).getPreviousPerson(100L);
        verify(taskService).startClassificationTask(createdPerson, Optional.of(previousPerson));
    }

    @Test
    void updatePersonAndStartTask_success() {
        // Given
        Long personId = 200L;
        PersonDTO dto = new PersonDTO();
        dto.setName("Jane");
        dto.setSurname("Smith");
        dto.setBirthDate(LocalDate.of(1995, 8, 15));
        dto.setCompany("Widgets");

        Person updatedPerson = new Person();
        updatedPerson.setId(personId);
        when(personService.updatePerson(personId, dto)).thenReturn(updatedPerson);

        when(personService.getPreviousPerson(personId))
                .thenReturn(Optional.empty());

        when(taskService.startClassificationTask(updatedPerson, Optional.empty()))
                .thenReturn("task-xyz");

        // When
        PersonResponse response = transactionService.updatePersonAndStartTask(personId, dto);

        // Then
        assertNotNull(response);
        assertEquals("200", response.getPersonId());
        assertEquals("task-xyz", response.getTaskId());
        assertEquals("Person Updated", response.getMessage());

        // Verify interactions
        verify(personService).updatePerson(personId, dto);
        verify(personService).getPreviousPerson(personId);
        verify(taskService).startClassificationTask(updatedPerson, Optional.empty());
    }
}
