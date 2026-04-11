package com.hrms.repository;

import com.hrms.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByEmployeeId(int employeeId);
    List<Attendance> findByEmployeeIdAndDateBetween(int employeeId, LocalDate from, LocalDate to);
}
