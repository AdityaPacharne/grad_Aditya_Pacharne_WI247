package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Employee;
import com.example.demo.repos.EmpRepo;

@RestController
public class EmpController {
	
	@Autowired
	EmpRepo repo;
	
	@GetMapping("/greet")
	public String hi() {
		return "<h2>Good Morning Everyone </h2>";
	}
	
	@GetMapping("/welcome")
	public String hello() {
		return "<h2>Have a good day!</h2>";
	}
	
	@GetMapping("/")
	public String abc() {
		return "<h2>Welcome to REST API Demo</h2>";
	}
	
	@GetMapping("/employees")
	public List<Employee> getEmployees() {
		return repo.findAll();
	}

	@PostMapping("/employees")
	public String getEmployees(@RequestBody Employee e) {
		if(repo.existsById(e.getEid())) {
			return "Sorry! Employee already exists";
		}
		repo.save(e);
		return "Successfully inserted Employee record";
	}
	
	@GetMapping("/employees/{id}")
	// You can also do @PathVariable("i")
	// if variable names are different
	public ResponseEntity<Employee> getEmployees(@PathVariable int id) {
		Optional<Employee> temp = repo.findById(id);
		if(temp.isEmpty()) return ResponseEntity.noContent().build();
		return ResponseEntity.ok(temp.get());
	}
	
	@PutMapping("/employees/{id}")
	public String updateEmployees(@PathVariable int id, @RequestBody Employee e) {
		if(e.getEid() != id) return "Emp IDs doesnt exist";
		if(!repo.existsById(id)) return "Sorry! Employee with given ID doesnt exist";
		repo.save(e);
		return "Successfully update employee record";
	}
	
	@DeleteMapping("/employees/{id}")
	public String removeEmployee(@PathVariable int id) {
		if(repo.existsById(id)) {
			repo.deleteById(id);
			return "Employee Removed successfully";
		}
		else return "No Record available with given ID";
	}
	
	@GetMapping("/employees/role/select")
	public List<Employee> retreiveBasedOnDesign(String desig) {
		return repo.findByDesignation(desig);
	}
	
	@GetMapping("/employees/earning/below")
	public List<Employee> earnings(int salary) {
		return repo.findBySalaryLessThan(salary);
	}
	
	@GetMapping("/employees/custom")
	public List<Employee> customQuery(@RequestParam("role") String desig) {
		return repo.myCustomQuery(desig);
	}
}
