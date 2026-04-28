package com.hrms.controller;

import com.hrms.model.Performance;
import com.hrms.service.PerformanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    /**
     * GET /api/performance
     * Trả về danh sách tất cả bản ghi performance (KPI).
     */
    @GetMapping("")
    public Page<Performance> listAll(@PageableDefault(size = 10) Pageable pageable) { return performanceService.getAll(pageable); }

    /**
     * GET /api/performance/{id}
     * Lấy chi tiết bản ghi performance theo id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Performance> get(@PathVariable int id) { return performanceService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    /**
     * GET /api/performance/employee/{empId}
     * Lấy tất cả bản ghi performance cho 1 nhân viên theo empId.
     */
    @GetMapping("/employee/{empId}")
    public List<Performance> byEmployee(@PathVariable int empId) { return performanceService.getByEmployee(empId); }

    /**
     * GET /api/performance/period/{period}
     * Lấy danh sách performance theo kỳ (ví dụ: 2026-03).
     */
    @GetMapping("/period/{period}")
    public List<Performance> byPeriod(@PathVariable String period) { return performanceService.getByPeriod(period); }

    /**
     * POST /api/performance
     * Tạo bản ghi performance mới. Body: Performance JSON (employeeId, period, kpiScore, rating, comments, reviewDate)
     */
    @PostMapping("")
    public Performance create(@RequestBody Performance p) { return performanceService.create(p); }

    /**
     * PUT /api/performance/{id}
     * Cập nhật thông tin performance theo id. Body: Performance JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<Performance> update(@PathVariable int id, @RequestBody Performance p) {
        if (performanceService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        p.setPerformanceId(id);
        return ResponseEntity.ok(performanceService.update(p));
    }

    /**
     * DELETE /api/performance/{id}
     * Xóa bản ghi performance theo id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) { performanceService.delete(id); return ResponseEntity.noContent().build(); }
}
