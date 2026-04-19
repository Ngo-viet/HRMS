package com.hrms.controller;

import com.hrms.model.Attendance;
import com.hrms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.time.YearMonth;
import java.util.Map;

import com.hrms.model.Salary;
import com.hrms.service.SalaryService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private SalaryService salaryService;

    /**
     * POST /api/attendance
     * Ghi nhận chấm công (thêm mới). Body: Attendance JSON (employeeId, date, hoursWorked, status, optional leaveId)
     */
    @PostMapping("")
    public Attendance createAttendance(@RequestBody Attendance a) {
        return attendanceService.register(a);
    }

    /**
     * GET /api/attendance/{id}
     * Lấy chi tiết một bản ghi chấm công.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getById(@PathVariable int id) {
        return attendanceService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/attendance/employee/{empId}
     * Lấy tất cả bản ghi chấm công của một nhân viên.
     */
    @GetMapping("/employee/{empId}")
    public List<Attendance> getByEmployee(@PathVariable int empId) {
        return attendanceService.fetchByEmployee(empId);
    }

    /**
     * GET /api/attendance/employee/{empId}/period?from=yyyy-MM-dd&to=yyyy-MM-dd
     * Lấy bản ghi chấm công cho một nhân viên trong khoảng thời gian.
     */
    @GetMapping("/employee/{empId}/period")
    public List<Attendance> getByEmployeeAndPeriod(@PathVariable int empId,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return attendanceService.fetchByEmployeeAndPeriod(empId, from, to);
    }

    /**
     * PUT /api/attendance/{id}
     * Cập nhật bản ghi chấm công.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Attendance> update(@PathVariable int id, @RequestBody Attendance a) {
        if (attendanceService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        a.setAttendanceId(id);
        return ResponseEntity.ok(attendanceService.edit(a));
    }

    /**
     * DELETE /api/attendance/{id}
     * Xóa bản ghi chấm công.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        attendanceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/attendance/employee/{empId}/summary?from=yyyy-MM-dd&to=yyyy-MM-dd
     * Trả về tổng hợp chấm công: totalPresentDays, totalHours, totalLeaves, totalAbsent
     */
    @GetMapping("/employee/{empId}/summary")
    public ResponseEntity<Map<String, Object>> getSummary(@PathVariable int empId,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<Attendance> list = attendanceService.fetchByEmployeeAndPeriod(empId, from, to);
        long present = list.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
        double totalHours = list.stream().filter(a -> a.getHoursWorked() != null).mapToDouble(Attendance::getHoursWorked).sum();
        long leaves = list.stream().filter(a -> "LEAVE".equalsIgnoreCase(a.getStatus())).count();
        long absent = list.stream().filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus())).count();

        Map<String, Object> res = Map.of(
                "totalPresentDays", present,
                "totalHours", totalHours,
                "totalLeaves", leaves,
                "totalAbsent", absent
        );
        return ResponseEntity.ok(res);
    }

    /**
     * POST /api/attendance/employee/{empId}/generate-salary?month=YYYY-MM
     * Tạo bản ghi salary cho employee dựa trên chấm công trong tháng. Body có thể cung cấp basic/hra/ca/deduction.
     * Body example: { "basic": "1500", "hra": "200", "ca": "100", "deduction": "50" }
     */
    @PostMapping("/employee/{empId}/generate-salary")
    public ResponseEntity<Salary> generateSalary(@PathVariable int empId,
                                                 @RequestParam String month,
                                                 @RequestBody Map<String, String> payload) {
        // parse month
        YearMonth ym = YearMonth.parse(month);
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();

        List<Attendance> list = attendanceService.fetchByEmployeeAndPeriod(empId, from, to);
        int totalWorkingDays = (int) list.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();

        Salary s = new Salary();
        s.setEid(empId);
        s.setMonth(month);
        s.setTotalWorkingDay(totalWorkingDays);
        s.setBasic(payload.getOrDefault("basic", "0"));
        s.setHra(payload.getOrDefault("hra", "0"));
        s.setCa(payload.getOrDefault("ca", "0"));
        s.setDeduction(payload.getOrDefault("deduction", "0"));
        // compute pay = basic + hra + ca - deduction (simple numeric strings)
        try {
            double basic = Double.parseDouble(s.getBasic());
            double hra = Double.parseDouble(s.getHra());
            double ca = Double.parseDouble(s.getCa());
            double deduction = Double.parseDouble(s.getDeduction());
            double net = basic + hra + ca - deduction;
            s.setPay(String.valueOf(net));
        } catch (Exception ex) {
            s.setPay("0");
        }

        Salary created = salaryService.register(s);
        return ResponseEntity.ok(created);
    }
}
