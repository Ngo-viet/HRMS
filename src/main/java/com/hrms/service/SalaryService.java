package com.hrms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrms.model.Leaves;
import com.hrms.model.Salary;
import com.hrms.repository.SalaryRepository;

@Service
public class SalaryService {

	@Autowired
	private SalaryRepository repo;

	// add salary
	public Salary register(Salary salary) {

		return repo.save(salary);
	}
	
	//fetch salary
	public List<Salary> fetchSalary()
	{
	return repo.findAll();	
	}
	
	// get Salary by ID
		public Optional<Salary> getById(int sid) {
			return repo.findById(sid);
		}

		// update Salary
		public Salary editSalary(Salary salary) {
			return repo.save(salary);
		}

		//delete Salary
		public String deleteSalary(int sid) {
			String result;
			try {
				repo.deleteById(sid);
				result = "Employee Salary Record Deleted";
			} catch (Exception e) {
				result = "Employee salary with Sid is not Deleted";
				e.printStackTrace();

			}
			return result;

		}
	

}
