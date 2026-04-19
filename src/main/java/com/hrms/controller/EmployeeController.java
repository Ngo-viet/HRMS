package com.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hrms.model.Employee;
import com.hrms.service.EmployeeService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService eservice;

    /**
     * GET /api/employeereport
     * Trả về danh sách tất cả nhân viên.
     */
    @GetMapping("/employeereport")
    public List<Employee> getemployees() {
        return eservice.fetchEmployees();
    }

    /**
     * POST /api/addemployee
     * Tạo mới một nhân viên. Body: Employee JSON. Kiểm tra trùng email trước khi tạo.
     */
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

    /**
     * GET /api/editemployee/{id}
     * Lấy thông tin employee theo id để edit.
     */
    @GetMapping("/editemployee/{id}")
    public Employee getEmpById(@PathVariable int id) {
        return eservice.getById(id).get();
    }

    /**
     * POST /api/editemployee
     * Cập nhật thông tin employee. Body: Employee JSON
     */
    @PostMapping("/editemployee")
    public Employee editEmployee(@RequestBody Employee employee) {
        return eservice.editEmployee(employee);
    }

    /**
     * GET /api/deleteemployee/{id}
     * Xóa employee theo id.
     */
    @GetMapping("/deleteemployee/{id}")
    public void deleteEmployee(@PathVariable int id) {
        eservice.deleteEmp(id);
    }

    /**
     * GET /api/searchemail/{email}
     * Tìm employee theo email.
     */
    @GetMapping("/searchemail/{email}")
    public Employee findEmployee(@PathVariable String email) {
        System.out.println(email);
        return eservice.fetchEmployeeByEmail(email);
    }
}