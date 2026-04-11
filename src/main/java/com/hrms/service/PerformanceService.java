package com.hrms.service;

import com.hrms.model.Performance;
import com.hrms.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceRepository repository;

    public Performance create(Performance p) { return repository.save(p); }
    public Optional<Performance> getById(int id) { return repository.findById(id); }
    public List<Performance> getByEmployee(int empId) { return repository.findByEmployeeId(empId); }
    public List<Performance> getByPeriod(String period) { return repository.findByPeriod(period); }
    public Performance update(Performance p) { return repository.save(p); }
    public void delete(int id) { repository.deleteById(id); }
}
