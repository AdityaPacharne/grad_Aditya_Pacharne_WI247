package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Student;
import com.example.demo.repos.h2.H2Repo;
import com.example.demo.repos.postgres.PostgresRepo;


@Controller
public class StudentController {
	
	@Autowired
	H2Repo repo;
	
	@Autowired
	PostgresRepo post;
	
	@ResponseBody
	@RequestMapping("/addStudent")
	public String addStudent(Student s) {
		System.out.println(s);
		repo.save(s);
		post.save(s);
		return "<h2>Student Record Added</h2>";
	}
}
