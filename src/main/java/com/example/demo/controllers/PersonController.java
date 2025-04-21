package com.example.demo.controllers;

import com.example.demo.dto.PersonDTO;
import com.example.demo.dto.PersonResponse;
import com.example.demo.mappers.PersonMapper;
import com.example.demo.services.PersonService;
import com.example.demo.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<EntityModel<PersonResponse>> createPerson(
            @Valid @RequestBody PersonDTO request) {
        PersonResponse response = transactionService.createPersonAndStartTask(request);

        EntityModel<PersonResponse> resource = EntityModel.of(response,
                linkTo(methodOn(PersonController.class)
                        .getPerson(Long.parseLong(response.getPersonId())))
                        .withSelfRel(),
                linkTo(methodOn(PersonController.class)
                        .updatePerson(Long.parseLong(response.getPersonId()), request)).withRel("update"));

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<EntityModel<PersonDTO>> getPerson(@PathVariable("personId") Long personId) {
        PersonDTO person = PersonMapper.toDto(personService.getPerson(personId));

        EntityModel<PersonDTO> resource = EntityModel.of(person,
                linkTo(methodOn(PersonController.class).getPerson(personId)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @PatchMapping("/{personId}")
    public ResponseEntity<EntityModel<PersonResponse>> updatePerson(
            @PathVariable("personId") Long personId,
            @RequestBody PersonDTO request) {
        PersonResponse updated = transactionService.updatePersonAndStartTask(personId, request);

        EntityModel<PersonResponse> resource = EntityModel.of(updated,
                linkTo(methodOn(PersonController.class).getPerson(personId)).withSelfRel());

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<CollectionModel<EntityModel<PersonDTO>>> getAll() {
        List<PersonDTO> people = personService.getAll();

        List<EntityModel<PersonDTO>> personResources = people.stream()
                .map(person -> EntityModel.of(person,
                        linkTo(methodOn(PersonController.class)
                                .getPerson(Long.parseLong(person.getPersonId()))).withSelfRel(),
                        linkTo(methodOn(PersonController.class)
                                .updatePerson(Long.parseLong(person.getPersonId()), null)).withRel("update")
                ))
                .toList();

        CollectionModel<EntityModel<PersonDTO>> collectionModel = CollectionModel.of(
                personResources,
                linkTo(methodOn(PersonController.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);

    }
}
