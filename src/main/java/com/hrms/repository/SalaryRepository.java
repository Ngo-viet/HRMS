package com.hrms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hrms.model.Salary;
import com.hrms.model.SalaryDTO;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {

    /**
     * Lấy danh sách lương kèm tên nhân viên bằng cách JOIN với bảng HRM_EMPLOYEE.
     * Dùng JPQL: join Salary.eid = Employee.id
     */
    @Query("""
        SELECT new com.hrms.model.SalaryDTO(
            s.salaryId, s.eid,
            e.firstName, e.lastName,
            s.month, s.totalWorkingDay,
            s.basic, s.hra, s.ca, s.pay, s.deduction
        )
        FROM Salary s
        JOIN Employee e ON s.eid = e.id
        """)
    Page<SalaryDTO> findAllWithEmployeeName(Pageable pageable);
}
