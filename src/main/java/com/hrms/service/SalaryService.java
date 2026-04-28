package com.hrms.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrms.model.Salary;
import com.hrms.model.SalaryDTO;
import com.hrms.repository.SalaryRepository;

@Service
public class SalaryService {

	@Autowired
	private SalaryRepository repo;

	// add salary
	public Salary register(Salary salary) {
		return repo.save(salary);
	}

	// fetch salary (trả về raw Salary, dùng nội bộ nếu cần)
	public Page<Salary> fetchSalary(Pageable pageable) {
		return repo.findAll(pageable);
	}

	// fetch salary kèm tên nhân viên
	public Page<SalaryDTO> fetchSalaryWithEmployeeName(Pageable pageable) {
		return repo.findAllWithEmployeeName(pageable);
	}

	// get Salary by ID
	public Optional<Salary> getById(int sid) {
		return repo.findById(sid);
	}

	// update Salary
	public Salary editSalary(Salary salary) {
		return repo.save(salary);
	}

	// delete Salary
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
