package com.hrms.service;

import com.hrms.model.Overtime;
import com.hrms.repository.OvertimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OvertimeService {

    @Autowired
    private OvertimeRepository repo;

    public Overtime register(Overtime o) { return repo.save(o); }
    public Optional<Overtime> getById(int id) { return repo.findById(id); }
    public List<Overtime> fetchByEmployee(int empId) { return repo.findByEmployeeId(empId); }
    public List<Overtime> fetchByEmployeeAndPeriod(int empId, LocalDate from, LocalDate to) { return repo.findByEmployeeIdAndDateBetween(empId, from, to); }
    public Overtime edit(Overtime o) { return repo.save(o); }
    public void delete(int id) { repo.deleteById(id); }
}
