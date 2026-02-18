package com.example.demo.repos.postgres;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Student;

public interface PostgresRepo extends JpaRepository<Student, Integer>{

}
