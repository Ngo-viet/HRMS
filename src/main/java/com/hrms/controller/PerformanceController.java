package com.hrms.controller;

import com.hrms.model.Performance;
import com.hrms.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    /**
     * GET /performance
     * Trả về danh sách tất cả bản ghi performance (KPI).
     */
    @GetMapping("/performance")
    public List<Performance> listAll() { return performanceService.getByPeriod(null); }

    /**
     * GET /performance/{id}
     * Lấy chi tiết bản ghi performance theo id.
     */
    @GetMapping("/performance/{id}")
    public ResponseEntity<Performance> get(@PathVariable int id) { return performanceService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    /**
     * GET /performance/employee/{empId}
     * Lấy tất cả bản ghi performance cho 1 nhân viên theo empId.
     */
    @GetMapping("/performance/employee/{empId}")
    public List<Performance> byEmployee(@PathVariable int empId) { return performanceService.getByEmployee(empId); }

    /**
     * GET /performance/period/{period}
     * Lấy danh sách performance theo kỳ (ví dụ: 2026-03).
     */
    @GetMapping("/performance/period/{period}")
    public List<Performance> byPeriod(@PathVariable String period) { return performanceService.getByPeriod(period); }

    /**
     * POST /performance
     * Tạo bản ghi performance mới. Body: Performance JSON (employeeId, period, kpiScore, rating, comments, reviewDate)
     */
    @PostMapping("/performance")
    public Performance create(@RequestBody Performance p) { return performanceService.create(p); }

    /**
     * PUT /performance/{id}
     * Cập nhật thông tin performance theo id. Body: Performance JSON
     */
    @PutMapping("/performance/{id}")
    public ResponseEntity<Performance> update(@PathVariable int id, @RequestBody Performance p) {
        if (performanceService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        p.setPerformanceId(id);
        return ResponseEntity.ok(performanceService.update(p));
    }

    /**
     * DELETE /performance/{id}
     * Xóa bản ghi performance theo id.
     */
    @DeleteMapping("/performance/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) { performanceService.delete(id); return ResponseEntity.noContent().build(); }
}
