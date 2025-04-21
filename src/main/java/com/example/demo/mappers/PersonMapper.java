package com.example.demo.mappers;

import com.example.demo.dto.PersonDTO;
import com.example.demo.models.Person;

public class PersonMapper {

    public static Person toEntity(PersonDTO dto) {
        if (dto == null) return null;

        Person person = new Person();
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setBirthDate(dto.getBirthDate());
        person.setCompany(dto.getCompany());
        return person;
    }

    public static PersonDTO toDto(Person person) {
        if (person == null) return null;

        PersonDTO dto = new PersonDTO();
        dto.setPersonId(person.getId().toString());
        dto.setName(person.getName());
        dto.setSurname(person.getSurname());
        dto.setBirthDate(person.getBirthDate());
        dto.setCompany(person.getCompany());
        return dto;
    }

    public static void updateEntityFromDto(PersonDTO dto, Person person) {
        if (dto.getName() != null) person.setName(dto.getName());
        if (dto.getSurname() != null) person.setSurname(dto.getSurname());
        if (dto.getBirthDate() != null) person.setBirthDate(dto.getBirthDate());
        if (dto.getCompany() != null) person.setCompany(dto.getCompany());
    }
}
