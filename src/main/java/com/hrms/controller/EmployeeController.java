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
import com.hrms.service.EmployeeService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeService eservice;

    // Get list of employee
    @GetMapping("/employeereport")
    public List<Employee> getemployees() {
        return eservice.fetchEmployees();
    }

    // Register Employee
    @PostMapping("/addemployee")
    public Employee registerEmployee(@RequestBody Employee employee) throws Exception {
        String tempEmail = employee.getEmail();
        if (tempEmail != null && !"".equals(tempEmail)) {
            Employee employeeObj = eservice.fetchEmployeeByEmail(tempEmail);
            if (employeeObj != null) {
                throw new Exception("User with " + tempEmail + " allready exist ");
            }
        }
        Employee employeeObj = eservice.register(employee);
        return employeeObj;
    }

    // get employee by id
    @GetMapping("/editemployee/{id}")
    public Employee getEmpById(@PathVariable int id) {
        return eservice.getById(id).get();
    }

    // edit employee
    @PostMapping("/editemployee")
    public Employee editEmployee(@RequestBody Employee employee) {
        return eservice.editEmployee(employee);
    }

    // delete employee by id
    @GetMapping("/deleteemployee/{id}")
    public void deleteEmployee(@PathVariable int id) {
        eservice.deleteEmp(id);
    }

    // search employee by email id
    @GetMapping("/searchemail/{email}")
    public Employee findEmployee(@PathVariable String email) {
        System.out.println(email);
        return eservice.fetchEmployeeByEmail(email);
    }
}