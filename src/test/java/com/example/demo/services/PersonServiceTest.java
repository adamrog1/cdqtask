package com.example.demo.services;

import com.example.demo.dto.PersonDTO;
import com.example.demo.exceptions.PersonNotFoundException;
import com.example.demo.mappers.PersonMapper;
import com.example.demo.models.Person;
import com.example.demo.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {
    private PersonRepository personRepository;
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personRepository = mock(PersonRepository.class);
        personService = new PersonService(personRepository);
    }

    @Test
    void createPerson_savesAndReturnsEntity() {
        // Given
        PersonDTO dto = new PersonDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setCompany("Acme");

        Person mapped = PersonMapper.toEntity(dto);
        when(personRepository.save(any(Person.class))).thenReturn(mapped);

        // When
        Person result = personService.createPerson(dto);

        // Then
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(captor.capture());
        Person saved = captor.getValue();
        assertEquals("John", saved.getName());
        assertEquals("Doe", saved.getSurname());
        assertEquals(LocalDate.of(1990, 1, 1), saved.getBirthDate());
        assertEquals("Acme", saved.getCompany());
    }

    @Test
    void getPerson_existingId_returnsPerson() {
        // Given
        Person person = new Person();
        person.setId(1L);
        person.setName("Alice");
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        // When
        Person result = personService.getPerson(1L);

        // Then
        assertEquals(person, result);
        verify(personRepository).findById(1L);
    }

    @Test
    void getPerson_unknownId_throwsException() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(PersonNotFoundException.class, () -> personService.getPerson(99L));
    }

    @Test
    void getPreviousPerson_delegatesToRepository() {
        // Given
        Person prev = new Person();
        when(personRepository.findFirstByIdLessThanOrderByIdDesc(5L))
                .thenReturn(Optional.of(prev));

        // When
        Optional<Person> result = personService.getPreviousPerson(5L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(prev, result.get());
    }

    @Test
    void updatePerson_existingId_updatesAndReturnsOriginal() {
        // Given
        Person existing = new Person();
        existing.setId(2L);
        existing.setName("Old");
        when(personRepository.findById(2L)).thenReturn(Optional.of(existing));

        PersonDTO dto = new PersonDTO();
        dto.setName("New");
        dto.setSurname("Name");
        dto.setBirthDate(LocalDate.of(2000, 2, 2));
        dto.setCompany("NewCo");

        // When
        Person result = personService.updatePerson(2L, dto);

        // Then
        assertEquals(existing, result);
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(captor.capture());
        Person saved = captor.getValue();
        assertEquals("New", saved.getName());
        assertEquals("Name", saved.getSurname());
        assertEquals(LocalDate.of(2000, 2, 2), saved.getBirthDate());
        assertEquals("NewCo", saved.getCompany());
    }

    @Test
    void updatePerson_unknownId_throwsException() {
        when(personRepository.findById(3L)).thenReturn(Optional.empty());
        PersonDTO dto = new PersonDTO();
        assertThrows(PersonNotFoundException.class, () -> personService.updatePerson(3L, dto));
    }

    @Test
    void getAll_returnsMappedDTOList() {
        // Given
        Person p1 = new Person(); p1.setId(10L); p1.setName("A"); p1.setSurname("B"); p1.setBirthDate(LocalDate.of(1991,1,1)); p1.setCompany("C");
        Person p2 = new Person(); p2.setId(20L); p2.setName("X"); p2.setSurname("Y"); p2.setBirthDate(LocalDate.of(1992,2,2)); p2.setCompany("Z");
        when(personRepository.findAll()).thenReturn(List.of(p1, p2));

        // When
        List<PersonDTO> list = personService.getAll();

        // Then
        assertEquals(2, list.size());
        PersonDTO dto1 = list.get(0);
        assertEquals("10", dto1.getPersonId());
        assertEquals("A", dto1.getName());
        PersonDTO dto2 = list.get(1);
        assertEquals("20", dto2.getPersonId());
        assertEquals("X", dto2.getName());
    }
}
