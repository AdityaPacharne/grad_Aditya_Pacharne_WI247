package com.example.demo.repos.h2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Student;

public interface H2Repo extends JpaRepository<Student, Integer>{

}
