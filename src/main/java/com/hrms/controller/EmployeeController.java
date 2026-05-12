package com.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import com.hrms.model.Employee;
import com.hrms.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService eservice;

    /**
     * GET /api/employeereport
     * Trả về danh sách tất cả nhân viên.
     */
    @GetMapping("/search")
    public Page<Employee> getemployees(@RequestParam(required = false) String firstName,
                                       @RequestParam(required = false) String lastName,
                                       @RequestParam(required = false) String department,
                                       @RequestParam(required = false) String email,
                                       @PageableDefault(size = 10) Pageable pageable) {
        return eservice.fetchEmployees(firstName, lastName, department, email, pageable);
    }

    /**
     * GET /api/employees/all
     * Trả về toàn bộ danh sách nhân viên (không phân trang).
     */
    @GetMapping("/list")
    public List<Employee> getAllEmployees() {
        return eservice.fetchAllEmployees();
    }

    /**
     * POST /api/addemployee
     * Tạo mới một nhân viên. Body: Employee JSON. Kiểm tra trùng email trước khi tạo.
     */
    @PostMapping("/add")
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
    @GetMapping("/edit/{id}")
    public Employee getEmpById(@PathVariable int id) {
        return eservice.getById(id).get();
    }

    /**
     * POST /api/editemployee
     * Cập nhật thông tin employee. Body: Employee JSON
     */
    @PostMapping("/edit")
    public Employee editEmployee(@RequestBody Employee employee) {
        return eservice.editEmployee(employee);
    }

    /**
     * GET /api/deleteemployee/{id}
     * Xóa employee theo id.
     */
    @GetMapping("/delete/{id}")
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