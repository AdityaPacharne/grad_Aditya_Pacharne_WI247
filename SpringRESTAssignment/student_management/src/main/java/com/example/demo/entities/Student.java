package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
public class Student {
	@Id
	private int regno;
	private int rollno;
	private String name;
	private int standard;
	private String school;
	private String gender;
	private int percentage;
	
	public int getRegNo() {
		return regno;
	}
	public void setRegNo(int regNo) {
		this.regno = regNo;
	}
	public int getRollNo() {
		return rollno;
	}
	public void setRollNo(int rollNo) {
		this.rollno = rollNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStandard() {
		return standard;
	}
	public void setStandard(int standard) {
		this.standard = standard;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	@Override
	public String toString() {
		return "Student [regNo=" + regno + ", rollNo=" + rollno + ", name=" + name + ", standard=" + standard
				+ ", school=" + school + ", gender=" + gender + ", percentage=" + percentage + "]";
	}
	
	
}
