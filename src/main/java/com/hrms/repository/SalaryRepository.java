package com.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.model.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer>
{
	 //some specific methods

}
