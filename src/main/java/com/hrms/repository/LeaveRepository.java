package com.hrms.repository;

import com.hrms.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrms.model.Leaves;

@Repository
public interface LeaveRepository extends JpaRepository<Leaves, Integer> 
{
	 //some specific methods
	 public Employee findEmailByEid(int id);

}
