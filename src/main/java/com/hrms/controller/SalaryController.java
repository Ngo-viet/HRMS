package com.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.model.Salary;
import com.hrms.service.SalaryService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SalaryController {

    @Autowired
    private SalaryService sservice;

    // get list of salary
    @GetMapping("/salaryreport")
    public List<Salary> getSalary() {
        return sservice.fetchSalary();
    }

    // add salary
    @PostMapping("/addsalary")
    public Salary addsalary(@RequestBody Salary salary) {
        return sservice.register(salary);
    }

    // get employee salary by id
    @GetMapping("/editsalary/{sid}")
    public Salary getSalaryById(@PathVariable int sid) {
        return sservice.getById(sid).get();
    }

    // Edit Salary
    @PostMapping("/editsalary")
    public Salary editSalary(@RequestBody Salary salary) {
        return sservice.editSalary(salary);
    }

    // delete Salary by id
    @GetMapping("/deletesalary/{sid}")
    public void deleteSalary(@PathVariable int sid) {
        sservice.deleteSalary(sid);
    }
}