package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Employee;

public interface EmpRepo extends JpaRepository<Employee, Integer> {
	public List<Employee> findByDesignation(String desig);
	
	public List<Employee> findBySalaryLessThan(int salary);
	
	// This is a hql query where hibernate will internally convert to SQL
	// HQL doesnt need "select *" because the object returned have all the attributes
	@Query("from Employee where designation=?1 order by salary desc")
	public List<Employee> myCustomQuery(String desig);
}
