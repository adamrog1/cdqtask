package com.example.demo.services;

import com.example.demo.dto.PersonDTO;
import com.example.demo.exceptions.PersonNotFoundException;
import com.example.demo.mappers.PersonMapper;
import com.example.demo.models.Person;
import com.example.demo.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    @Transactional
    public Person createPerson(PersonDTO personDTO) {
        Person person = PersonMapper.toEntity(personDTO);
        personRepository.save(person);
        return person;
    }

    @Cacheable(value = "personCache", key = "#personId")
    public Person getPerson(Long personId) {
        return personRepository.findById(personId).orElseThrow(() -> new PersonNotFoundException(personId));
    }

    public Optional<Person> getPreviousPerson(Long personId) {
        return personRepository.findFirstByIdLessThanOrderByIdDesc(personId);
    }

    public Person updatePerson(Long personId, PersonDTO request) {
        Person person = personRepository.findById(personId).orElseThrow(() -> new PersonNotFoundException(personId));
        personRepository.save(PersonMapper.toEntity(request));
        return person;
    }

    @Cacheable("people")
    public List<PersonDTO> getAll() {
        return personRepository.findAll()
                .stream()
                .map(PersonMapper::toDto)
                .collect(Collectors.toList());
    }
}
