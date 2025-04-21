package com.example.demo.repositories;

import com.example.demo.models.Person;
import com.example.demo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByTaskId(String id);
}
