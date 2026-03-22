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

    /**
     * GET /salaryreport
     * Trả về danh sách các bản ghi lương.
     */
    @GetMapping("/salaryreport")
    public List<Salary> getSalary() {
        return sservice.fetchSalary();
    }

    /**
     * POST /addsalary
     * Thêm bản ghi lương mới. Body: Salary JSON
     */
    @PostMapping("/addsalary")
    public Salary addsalary(@RequestBody Salary salary) {
        return sservice.register(salary);
    }

    /**
     * GET /editsalary/{sid}
     * Lấy bản ghi lương theo id.
     */
    @GetMapping("/editsalary/{sid}")
    public Salary getSalaryById(@PathVariable int sid) {
        return sservice.getById(sid).get();
    }

    /**
     * POST /editsalary
     * Cập nhật bản ghi lương. Body: Salary JSON
     */
    @PostMapping("/editsalary")
    public Salary editSalary(@RequestBody Salary salary) {
        return sservice.editSalary(salary);
    }

    /**
     * GET /deletesalary/{sid}
     * Xóa bản ghi lương theo id.
     */
    @GetMapping("/deletesalary/{sid}")
    public void deleteSalary(@PathVariable int sid) {
        sservice.deleteSalary(sid);
    }
}