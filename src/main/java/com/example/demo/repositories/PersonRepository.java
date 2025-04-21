package com.example.demo.repositories;

import com.example.demo.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findById(Long id);
    Optional<Person> findFirstByIdLessThanOrderByIdDesc(Long currentId);
}
