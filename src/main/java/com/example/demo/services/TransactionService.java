package com.example.demo.services;

import com.example.demo.dto.PersonDTO;
import com.example.demo.dto.PersonResponse;
import com.example.demo.models.Person;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final PersonService personService;
    private final TaskService taskService;

    @Transactional
    public PersonResponse createPersonAndStartTask(PersonDTO personRequest) {
        Person personResponse = personService.createPerson(personRequest);
        Optional<Person> previousPerson = personService.getPreviousPerson(personResponse.getId());
        String taskId = taskService.startClassificationTask(personResponse, previousPerson);
        return new PersonResponse(personResponse.getId().toString(), taskId, "Person Created");
    }

    @Transactional
    public PersonResponse updatePersonAndStartTask(Long personId, PersonDTO personRequest) {
        Person personResponse = personService.updatePerson(personId, personRequest);
        Optional<Person> previousPerson = personService.getPreviousPerson(personResponse.getId());
        String taskId = taskService.startClassificationTask(personResponse, previousPerson);
        return new PersonResponse(personResponse.getId().toString(), taskId, "Person Updated");
    }
}
