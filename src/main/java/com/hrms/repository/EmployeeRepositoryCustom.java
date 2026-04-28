package com.hrms.repository;

import com.hrms.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeRepositoryCustom {
    Page<Employee> searchEmployees(String firstName, String lastName, String department, String email, Pageable pageable);
}
