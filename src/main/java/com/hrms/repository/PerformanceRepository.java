package com.hrms.repository;

import com.hrms.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {
    List<Performance> findByEmployeeId(int employeeId);
    List<Performance> findByPeriod(String period);
}
