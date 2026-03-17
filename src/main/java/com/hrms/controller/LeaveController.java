package com.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.model.Employee;
import com.hrms.model.Leaves;
import com.hrms.service.LeaveService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LeaveController {

    @Autowired
    private LeaveService lservice;

    // get list of leaves
    @GetMapping("/leavesreport")
    public List<Leaves> getLeaves() {
        return lservice.fetchLeaves();
    }

    // add leaves
    @PostMapping("/addleaves")
    public Leaves addLeaves(@RequestBody Leaves leaves) throws Exception {
        int tempId = leaves.getEmployeeId();
        System.out.println("TempEid: " + tempId);
        Employee emp = lservice.findEmailByEid(tempId);

        if (emp.getEmail() != null) {
            leaves.setEmail(emp.getEmail());
            return lservice.register(leaves);
        } else {
            throw new Exception("Employee with " + tempId + " Not exists ");
        }
    }

    // get leave by id
    @GetMapping("/editleaves/{id}")
    public Leaves getLeavesById(@PathVariable int id) {
        return lservice.getById(id).get();
    }

    // edit Leaves
    @PostMapping("/editleaves")
    public Leaves editLeave(@RequestBody Leaves leaves) {
        return lservice.editLeaves(leaves);
    }

    // delete Leaves by id
    @GetMapping("/deleteleaves/{id}")
    public void deleteLeaves(@PathVariable int id) {
        lservice.deleteleaves(id);
    }
}