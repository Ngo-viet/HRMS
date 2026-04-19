package com.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hrms.model.Salary;
import com.hrms.service.SalaryService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalaryService sservice;

    /**
     * GET /api/salary/report
     * Trả về danh sách các bản ghi lương.
     */
    @GetMapping("/report")
    public List<Salary> getSalary() {
        return sservice.fetchSalary();
    }

    /**
     * POST /api/salary
     * Thêm bản ghi lương mới. Body: Salary JSON
     */
    @PostMapping("")
    public Salary addsalary(@RequestBody Salary salary) {
        return sservice.register(salary);
    }

    /**
     * GET /api/salary/{sid}
     * Lấy bản ghi lương theo id.
     */
    @GetMapping("/{sid}")
    public Salary getSalaryById(@PathVariable int sid) {
        return sservice.getById(sid).get();
    }

    /**
     * PUT /api/salary/edit
     * Cập nhật bản ghi lương. Body: Salary JSON
     */
    @PostMapping("/edit")
    public Salary editSalary(@RequestBody Salary salary) {
        return sservice.editSalary(salary);
    }

    /**
     * DELETE /api/salary/{sid}
     * Xóa bản ghi lương theo id.
     */
    @GetMapping("/delete/{sid}")
    public void deleteSalary(@PathVariable int sid) {
        sservice.deleteSalary(sid);
    }
}