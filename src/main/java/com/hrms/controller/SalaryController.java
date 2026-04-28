package com.hrms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hrms.model.Salary;
import com.hrms.model.SalaryDTO;
import com.hrms.service.SalaryService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalaryService sservice;

    /**
     * GET /api/salary/search
     * Trả về danh sách lương kèm tên nhân viên (firstName, lastName).
     */
    @GetMapping("/search")
    public Page<SalaryDTO> getSalary(@PageableDefault(size = 10) Pageable pageable) {
        return sservice.fetchSalaryWithEmployeeName(pageable);
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
     * POST /api/salary/edit
     * Cập nhật bản ghi lương. Body: Salary JSON
     */
    @PostMapping("/edit")
    public Salary editSalary(@RequestBody Salary salary) {
        return sservice.editSalary(salary);
    }

    /**
     * GET /api/salary/delete/{sid}
     * Xóa bản ghi lương theo id.
     */
    @GetMapping("/delete/{sid}")
    public void deleteSalary(@PathVariable int sid) {
        sservice.deleteSalary(sid);
    }
}