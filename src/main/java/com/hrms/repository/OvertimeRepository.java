package com.hrms.repository;

import com.hrms.model.Overtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvertimeRepository extends JpaRepository<Overtime, Integer> {
    List<Overtime> findByEmployeeId(int employeeId);
    List<Overtime> findByEmployeeIdAndDateBetween(int employeeId, LocalDate from, LocalDate to);
}
