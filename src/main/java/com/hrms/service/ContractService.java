package com.hrms.service;

import com.hrms.model.Contract;
import com.hrms.repository.ContractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository repo;

    public Contract create(Contract c) { return repo.save(c); }
    public Optional<Contract> getById(int id) { return repo.findById(id); }
    public List<Contract> getByEmployee(int empId) { return repo.findByEmployeeId(empId); }
    public Page<Contract> getAll(Pageable pageable) { return repo.findAll(pageable); }
    public List<Contract> getAllList() { return repo.findAll(); }
    public Contract update(Contract c) { return repo.save(c); }
    public void delete(int id) { repo.deleteById(id); }
}
