package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Student;
import com.example.demo.repos.StudentRepo;

/*
GET		/students									- Get all the students
GET 	/students/regNo								- Specific student details of the given Registration number
POST	/students									- Insert a student record
PUT		/students/regNo								- Update specific student record
PATCH 	/students/regNo								- Update with given attributes
DELETE	/students/regNo								- Remove the student record for the given Registration number
GET		/students/school?name=KV					- List all students belonging to that school
GET		/students/school/count?name=DPS				- Total strength in that school
GET		/students/school/standard/count?class=5		- Total number of students in 5th standard
GET 	/students/result?pass=true/false			- List the students in descending order of their percentage (40% and above is pass)
GET		/students/strength?gender=MALE&standard=5	- How many Male students in standard 5
 */

@RestController
public class StudentController {
	
	@Autowired
	StudentRepo repo;
	
	@GetMapping("/students")
	public List<Student> getAllStudents() {
		return repo.findAll();
	}
	
	@GetMapping("/students/{regno}")
	public Optional<Student> getOneStudent(@PathVariable int regno) {
		return repo.findById(regno);
	}
	
	@PostMapping("/students")
	public String insertStudent(@RequestBody Student s) {
		if(repo.existsById(s.getRegNo())) {
			return "Sorry Student already exists";
		}
		else {
			try {
				repo.save(s);
				return "Succesfully inserted student record";
			}
			catch(Exception e) {
				return "Sorry Student already exists";
			}
		}
	}
	
	@DeleteMapping("/students")
	public String deleteOneStudent(int regNo) {
		if(repo.existsById(regNo)) {
			repo.deleteById(regNo);
			return "Succesfully deleted student record";
		}
		else {
			return "Sorry Student record not found";
		}
	}
	
	@GetMapping("/students/school")
	public List<Student> getStudentBySchool(@RequestParam("name") String school) {
		return repo.findBySchool(school);
	}
	
	@GetMapping("/students/school/count")
	public int getStudentCountBySchool(@RequestParam("name") String school) {
		return repo.findBySchool(school).size();
	}
	
	@GetMapping("/students/school/standard/count")
	public int getStudentCountByStandard(@RequestParam("class") int standard) {
		return repo.findByStandard(standard).size();
	}
	
	@GetMapping("/students/result")
	public List<Student> getStudentByResult(@RequestParam("pass") boolean result) {
		if(result) 	return repo.findByPercentageGreaterThanEqualOrderByPercentageDesc(40);
		else 		return repo.findByPercentageLessThanOrderByPercentageDesc(40);
	}
	
	@GetMapping("/students/strength")
	public int getStudentByGenderAndStandard(String gender, int standard) {
		return repo.findByGenderAndStandard(gender, standard).size();
	}
}
