package com.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.model.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> 
{
	//some specific method
	public Employee findByEmail(String tempEmail);

}
