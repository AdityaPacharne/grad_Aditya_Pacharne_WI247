package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {
	
	public List<Student> findBySchool(String school);
	
	public List<Student> findByStandard(int standard);
	
	public List<Student> findByGenderAndStandard(String gender, int standard);
	
	public List<Student> findByPercentageGreaterThanEqualOrderByPercentageDesc(int percentage);
	
	public List<Student> findByPercentageLessThanOrderByPercentageDesc(int percentage);
}
