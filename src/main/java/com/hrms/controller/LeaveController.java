package com.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hrms.model.Employee;
import com.hrms.model.Leaves;
import com.hrms.service.LeaveService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService lservice;

    /**
     * GET /api/leaves/report
     * Trả về danh sách tất cả các đơn nghỉ phép.
     */
    @GetMapping("/report")
    public List<Leaves> getLeaves() {
        return lservice.fetchLeaves();
    }

    /**
     * POST /api/leaves
     * Thêm mới đơn nghỉ phép cho employee. Body: Leaves JSON (cần employeeId).
     * Service sẽ tìm email của employee theo employeeId và gán vào đơn.
     */
    @PostMapping("")
    public Leaves addLeaves(@RequestBody Leaves leaves) throws Exception {
        int tempId = leaves.getEmployeeId();
        System.out.println("TempEid: " + tempId);
        Employee emp = lservice.findEmailByEid(tempId);

        if (emp.getEmail() != null) {
            leaves.setEmail(emp.getEmail());
            return lservice.register(leaves);
        } else {
            throw new Exception("Employee with " + tempId + " Not exists ");
        }
    }

    /**
     * GET /api/leaves/{id}
     * Lấy chi tiết 1 đơn nghỉ phép theo id.
     */
    @GetMapping("/{id}")
    public Leaves getLeavesById(@PathVariable int id) {
        return lservice.getById(id).get();
    }

    /**
     * PUT /api/leaves/{id}
     * Cập nhật đơn nghỉ phép. Body: Leaves JSON
     */
    @PostMapping("/edit")
    public Leaves editLeave(@RequestBody Leaves leaves) {
        return lservice.editLeaves(leaves);
    }

    /**
     * DELETE /api/leaves/{id}
     * Xóa đơn nghỉ phép theo id.
     */
    @GetMapping("/delete/{id}")
    public void deleteLeaves(@PathVariable int id) {
        lservice.deleteleaves(id);
    }

    /**
     * POST /api/leaves/{id}/approve?approver=NAME
     * Duyệt đơn nghỉ phép: đổi status -> APPROVED, ghi approvedBy, approvedDate
     */
    @PostMapping("/{id}/approve")
    public Leaves approveLeave(@PathVariable int id, @RequestParam String approver) throws Exception {
        var opt = lservice.getById(id);
        if (opt.isEmpty()) throw new Exception("Leave not found");
        var leave = opt.get();
        leave.setStatus("APPROVED");
        leave.setApprovedBy(approver);
        leave.setApprovedDate(java.time.LocalDate.now());
        return lservice.editLeaves(leave);
    }

    /**
     * POST /api/leaves/{id}/reject?approver=NAME
     * Từ chối đơn nghỉ phép
     */
    @PostMapping("/{id}/reject")
    public Leaves rejectLeave(@PathVariable int id, @RequestParam String approver) throws Exception {
        var opt = lservice.getById(id);
        if (opt.isEmpty()) throw new Exception("Leave not found");
        var leave = opt.get();
        leave.setStatus("REJECTED");
        leave.setApprovedBy(approver);
        leave.setApprovedDate(java.time.LocalDate.now());
        return lservice.editLeaves(leave);
    }

    /**
     * POST /api/leaves/approve/{id}
     * Duyệt đơn (API mới yêu cầu): approver là người thực hiện action (query param `approver`).
     */
    @PostMapping("/approve/{id}")
    public Leaves approveLeaveNew(@PathVariable int id, @RequestParam String approver) throws Exception {
        return lservice.approveLeave(id, approver);
    }

    /**
     * POST /api/leaves/reject/{id}
     * Từ chối đơn (API mới yêu cầu): approver query param.
     */
    @PostMapping("/reject/{id}")
    public Leaves rejectLeaveNew(@PathVariable int id, @RequestParam String approver) throws Exception {
        return lservice.rejectLeave(id, approver);
    }

    /**
     * GET /api/leaves/pending?approver=NAME
     * Lấy danh sách đơn đang chờ duyệt; `approver` là tên người gọi API, để log hoặc filter nếu cần.
     */
    @GetMapping("/pending")
    public java.util.Map<String, Object> getPendingLeaves(@RequestParam(required = false) String approver) {
        // Return wrapper with who requested and the pending leaves list
        System.out.println("Leaves pending requested by: " + approver);
        var list = lservice.fetchPendingLeaves();
        return java.util.Map.of(
                "requestedBy", approver == null ? "anonymous" : approver,
                "pendingLeaves", list
        );
    }
}