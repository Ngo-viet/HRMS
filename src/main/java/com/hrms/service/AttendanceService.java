package com.hrms.service;

import com.hrms.model.Attendance;
import com.hrms.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance register(Attendance a) {
        return attendanceRepository.save(a);
    }

    public Optional<Attendance> getById(int id) {
        return attendanceRepository.findById(id);
    }

    public List<Attendance> fetchByEmployee(int empId) {
        return attendanceRepository.findByEmployeeId(empId);
    }

    public List<Attendance> fetchByEmployeeAndPeriod(int empId, LocalDate from, LocalDate to) {
        return attendanceRepository.findByEmployeeIdAndDateBetween(empId, from, to);
    }

    public Attendance edit(Attendance a) {
        return attendanceRepository.save(a);
    }

    public void delete(int id) {
        attendanceRepository.deleteById(id);
    }
}
