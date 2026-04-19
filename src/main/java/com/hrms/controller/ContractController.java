package com.hrms.controller;

import com.hrms.model.Contract;
import com.hrms.service.ContractService;
import com.hrms.dto.ContractDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * GET /api/contracts
     * Trả về danh sách tất cả hợp đồng.
     */
    @GetMapping("")
    public List<Contract> listAll() { return contractService.getAll(); }

    /**
     * GET /api/contracts/{id}
     * Lấy chi tiết hợp đồng theo id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contract> get(@PathVariable int id) { return contractService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    /**
     * GET /api/contracts/employee/{empId}
     * Lấy tất cả hợp đồng của 1 nhân viên theo empId.
     */
    @GetMapping("/employee/{empId}")
    public List<Contract> byEmployee(@PathVariable int empId) { return contractService.getByEmployee(empId); }

    /**
     * POST /api/contracts
     * Tạo hợp đồng mới. Body: ContractDto JSON (employeeId, startDate, endDate, contractType, contractSalary, status, notes)
     */
    @PostMapping("")
    public Contract create(@Valid @RequestBody ContractDto dto) {
        Contract c = new Contract();
        c.setEmployeeId(dto.getEmployeeId());
        c.setStartDate(dto.getStartDate());
        c.setEndDate(dto.getEndDate());
        c.setContractType(dto.getContractType());
        c.setContractSalary(dto.getContractSalary());
        c.setStatus(dto.getStatus());
        c.setNotes(dto.getNotes());
        return contractService.create(c);
    }

    /**
     * PUT /api/contracts/{id}
     * Cập nhật hợp đồng theo id. Body: ContractDto JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contract> update(@PathVariable int id, @Valid @RequestBody ContractDto dto) {
        if (contractService.getById(id).isEmpty()) return ResponseEntity.notFound().build();
        Contract c = new Contract();
        c.setContractId(id);
        c.setEmployeeId(dto.getEmployeeId());
        c.setStartDate(dto.getStartDate());
        c.setEndDate(dto.getEndDate());
        c.setContractType(dto.getContractType());
        c.setContractSalary(dto.getContractSalary());
        c.setStatus(dto.getStatus());
        c.setNotes(dto.getNotes());
        return ResponseEntity.ok(contractService.update(c));
    }

    /**
     * DELETE /api/contracts/{id}
     * Xóa hợp đồng theo id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) { contractService.delete(id); return ResponseEntity.noContent().build(); }
}
