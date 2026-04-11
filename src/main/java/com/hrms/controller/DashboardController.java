package com.hrms.controller;

import com.hrms.model.Attendance;
import com.hrms.model.Employee;
import com.hrms.model.Leaves;
import com.hrms.model.Overtime;
import com.hrms.model.Performance;
import com.hrms.model.Salary;
import com.hrms.service.AttendanceService;
import com.hrms.service.ContractService;
import com.hrms.service.EmployeeService;
import com.hrms.service.LeaveService;
import com.hrms.service.OvertimeService;
import com.hrms.service.PerformanceService;
import com.hrms.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private OvertimeService overtimeService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private SalaryService salaryService;

    /**
     * GET /dashboard/summary?from=yyyy-MM-dd&to=yyyy-MM-dd&period=YYYY-MM
     * Trả về các chỉ số tổng quan cho dashboard trong khoảng thời gian.
     * - totalEmployees
     * - totalContracts
     * - pendingLeaves
     * - approvedLeaves
     * - totalPresentDays
     * - totalOtHours
     * - avgKpi (nếu period được cung cấp)
     * - salaryCount, totalNetPaid (nếu period được cung cấp)
     */
    @GetMapping("/summary")
    public Map<String, Object> summary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String period) {

        // default range: current month
        if (from == null || to == null) {
            LocalDate now = LocalDate.now();
            from = now.withDayOfMonth(1);
            to = now;
        }

        List<Employee> employees = employeeService.fetchEmployees();
        int totalEmployees = employees.size();
        int totalContracts = contractService.getAll().size();

        List<Leaves> leaves = leaveService.fetchLeaves();
        long pendingLeaves = leaves.stream().filter(l -> l.getStatus() == null || "PENDING".equalsIgnoreCase(l.getStatus())).count();
        long approvedLeaves = leaves.stream().filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus())).count();

        // attendance & OT aggregates across all employees in the period
        long totalPresentDays = 0;
        double totalOtHours = 0.0;
        for (Employee e : employees) {
            List<Attendance> atts = attendanceService.fetchByEmployeeAndPeriod(e.getId(), from, to);
            totalPresentDays += atts.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();

            List<Overtime> ots = overtimeService.fetchByEmployeeAndPeriod(e.getId(), from, to);
            totalOtHours += ots.stream().filter(o -> o.getHours() != null).mapToDouble(Overtime::getHours).sum();
        }

        double avgKpi = 0.0;
        if (period != null) {
            List<Performance> perf = performanceService.getByPeriod(period);
            avgKpi = perf.stream().filter(p -> p.getKpiScore() != null).mapToDouble(Performance::getKpiScore).average().orElse(0.0);
        }

        int salaryCount = 0;
        double totalNetPaid = 0.0;
        if (period != null) {
            List<Salary> salaries = salaryService.fetchSalary().stream().filter(s -> period.equals(s.getMonth())).collect(Collectors.toList());
            salaryCount = salaries.size();
            for (Salary s : salaries) {
                try {
                    totalNetPaid += Double.parseDouble(s.getPay() == null ? "0" : s.getPay());
                } catch (NumberFormatException ex) {
                    // ignore malformed pay
                }
            }
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("totalEmployees", totalEmployees);
        res.put("totalContracts", totalContracts);
        res.put("pendingLeaves", pendingLeaves);
        res.put("approvedLeaves", approvedLeaves);
        res.put("totalPresentDays", totalPresentDays);
        res.put("totalOtHours", totalOtHours);
        res.put("avgKpi", avgKpi);
        res.put("salaryCount", salaryCount);
        res.put("totalNetPaid", totalNetPaid);
        res.put("from", from.toString());
        res.put("to", to.toString());
        res.put("period", period);
        return res;
    }

    /**
     * GET /dashboard/attendance/summary?from=&to=&empId(optional)
     * Nếu empId được cung cấp trả summary cho emp đó, ngược lại trả list summary cho tất cả nhân viên.
     * Summary per employee: { empId, fullName, presentDays, totalHours, leaves, absent }
     */
    @GetMapping("/attendance/summary")
    public Object attendanceSummary(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                    @RequestParam(required = false) Integer empId) {
        if (from == null || to == null) {
            LocalDate now = LocalDate.now();
            from = now.withDayOfMonth(1);
            to = now;
        }

        if (empId != null) {
            var atts = attendanceService.fetchByEmployeeAndPeriod(empId, from, to);
            long present = atts.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
            double hours = atts.stream().filter(a -> a.getHoursWorked() != null).mapToDouble(Attendance::getHoursWorked).sum();
            long leavesCount = atts.stream().filter(a -> "LEAVE".equalsIgnoreCase(a.getStatus())).count();
            long absent = atts.stream().filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus())).count();
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("empId", empId);
            m.put("presentDays", present);
            m.put("totalHours", hours);
            m.put("leaves", leavesCount);
            m.put("absent", absent);
            m.put("from", from.toString());
            m.put("to", to.toString());
            return m;
        }

        List<Employee> employees = employeeService.fetchEmployees();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Employee e : employees) {
            var atts = attendanceService.fetchByEmployeeAndPeriod(e.getId(), from, to);
            long present = atts.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
            double hours = atts.stream().filter(a -> a.getHoursWorked() != null).mapToDouble(Attendance::getHoursWorked).sum();
            long leavesCount = atts.stream().filter(a -> "LEAVE".equalsIgnoreCase(a.getStatus())).count();
            long absent = atts.stream().filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus())).count();
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("empId", e.getId());
            m.put("fullName", e.getFirstName() + " " + e.getLastName());
            m.put("presentDays", present);
            m.put("totalHours", hours);
            m.put("leaves", leavesCount);
            m.put("absent", absent);
            list.add(m);
        }
        return list;
    }

    /**
     * GET /dashboard/leaves?status=&from=&to=
     * Trả về danh sách đơn nghỉ theo filter status và khoảng thời gian (so sánh from/to của đơn).
     */
    @GetMapping("/leaves")
    public List<Leaves> leavesReport(@RequestParam(required = false) String status,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<Leaves> all = leaveService.fetchLeaves();
        return all.stream().filter(l -> {
            boolean ok = true;
            if (status != null) ok = status.equalsIgnoreCase(l.getStatus());
            if (from != null) {
                try {
                    LocalDate lf = LocalDate.parse(l.getFromDate());
                    if (lf.isBefore(from)) ok = false;
                } catch (Exception ex) {
                    // ignore parse error
                }
            }
            if (to != null) {
                try {
                    LocalDate lt = LocalDate.parse(l.getToDate());
                    if (lt.isAfter(to)) ok = false;
                } catch (Exception ex) {}
            }
            return ok;
        }).collect(Collectors.toList());
    }

}
