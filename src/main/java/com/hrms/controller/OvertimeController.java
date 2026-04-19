package com.hrms.controller;

import com.hrms.model.Overtime;
import com.hrms.service.OvertimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/ot")
public class OvertimeController {

    @Autowired
    private OvertimeService overtimeService;

    /**
     * POST /api/ot
     * Tạo bản ghi OT. Body: Overtime JSON (employeeId, date, startTime, endTime, hours, reason)
     */
    @PostMapping("")
    public Overtime create(@RequestBody Overtime o) { return overtimeService.register(o); }

    /**
     * GET /api/ot/{id}
     * Lấy chi tiết OT theo id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Overtime> get(@PathVariable int id) { return overtimeService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    /**
     * GET /api/ot/employee/{empId}
     * Lấy danh sách OT của 1 nhân viên.
     */
    @GetMapping("/employee/{empId}")
    public List<Overtime> listByEmployee(@PathVariable int empId) { return overtimeService.fetchByEmployee(empId); }

    /**
     * GET /api/ot/employee/{empId}/period?from=yyyy-MM-dd&to=yyyy-MM-dd
     * Lấy OT trong khoảng thời gian cho nhân viên.
     */
    @GetMapping("/employee/{empId}/period")
    public List<Overtime> listByEmployeePeriod(@PathVariable int empId,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return overtimeService.fetchByEmployeeAndPeriod(empId, from, to);
    }

    /**
     * PUT /api/ot/{id}
     * Cập nhật OT.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Overtime> update(@PathVariable int id, @RequestBody Overtime o) {
        if (overtimeService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        o.setOtId(id); return ResponseEntity.ok(overtimeService.edit(o));
    }

    /**
     * DELETE /api/ot/{id}
     * Xóa OT theo id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) { overtimeService.delete(id); return ResponseEntity.noContent().build(); }

    /**
     * POST /api/ot/{id}/approve?approver=NAME
     * Duyệt OT: đổi status -> APPROVED, lưu approver và date.
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<Overtime> approve(@PathVariable int id, @RequestParam String approver) throws Exception {
        var opt = overtimeService.getById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var o = opt.get();
        o.setStatus("APPROVED"); o.setApprovedBy(approver); o.setApprovedDate(java.time.LocalDate.now());
        return ResponseEntity.ok(overtimeService.edit(o));
    }

    /**
     * POST /api/ot/{id}/reject?approver=NAME
     * Từ chối OT: đổi status -> REJECTED, lưu approver và date.
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<Overtime> reject(@PathVariable int id, @RequestParam String approver) throws Exception {
        var opt = overtimeService.getById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var o = opt.get();
        o.setStatus("REJECTED"); o.setApprovedBy(approver); o.setApprovedDate(java.time.LocalDate.now());
        return ResponseEntity.ok(overtimeService.edit(o));
    }
}
