package com.hrms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrms.model.Employee;
import com.hrms.repository.EmployeeRepository;

@Service
public class EmployeeService 
{
	@Autowired
	private EmployeeRepository repo;
	
	//register Employee
		public Employee register(Employee employee)
		{
			//this method save value in db and return Employee object
			return repo.save(employee);
		}
		
		// fetch Email id
		public Employee fetchEmployeeByEmail(String tempEmail)
		{
			return repo.findByEmail(tempEmail);
		}
		
		//fetch Employees List		
		public List<Employee>fetchEmployees()
		{
			return repo.findAll();
		}
	
		//get Emp by ID
		public Optional<Employee> getById(int id)
		{	
			return repo.findById(id);	
		}
		
		//update Employee
		public Employee editEmployee(Employee employee)
		{
			return repo.save(employee);
		}
		
		//delete employee by id
		public String deleteEmp(int id)
		{
			String result;
			try
			{
				repo.deleteById(id);
				result="Employee Record Deleted";
			}
			catch(Exception e)
			{
				result="Employee with Id is not Deleted";
				e.printStackTrace();
				
			}
			return result;
		}
 
		public Optional<Employee> getemp(int id)
		{
			return repo.findById(id);
		}
}
