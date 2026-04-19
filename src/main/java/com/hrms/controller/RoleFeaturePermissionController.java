package com.hrms.controller;

import com.hrms.model.RoleFeaturePermission;
import com.hrms.service.RoleFeaturePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/rfp")
public class RoleFeaturePermissionController {

    @Autowired
    private RoleFeaturePermissionService rfpService;

    /**
     * GET /api/rfp
     * Lấy danh sách tất cả bản ghi Role-Feature-Permission.
     */
    @GetMapping("")
    public List<RoleFeaturePermission> getAll() {
        return rfpService.fetchAll();
    }

    /**
     * GET /api/rfp/{id}
     * Lấy chi tiết 1 bản ghi RFP theo id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleFeaturePermission> getById(@PathVariable int id) {
        return rfpService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/rfp
     * Tạo bản ghi Role-Feature-Permission mới. Body: RoleFeaturePermission JSON (role, feature, flags).
     */
    @PostMapping("")
    public RoleFeaturePermission create(@RequestBody RoleFeaturePermission rfp) {
        return rfpService.create(rfp);
    }

    /**
     * PUT /api/rfp/{id}
     * Cập nhật RFP theo id.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleFeaturePermission> update(@PathVariable int id, @RequestBody RoleFeaturePermission rfp) {
        if (!rfpService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rfp.setId(id);
        return ResponseEntity.ok(rfpService.update(rfp));
    }

    /**
     * DELETE /api/rfp/{id}
     * Xóa bản ghi RFP.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        rfpService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/roles/{roleId}/rfp
     * Lấy tất cả RFP cho 1 role.
     */
    @GetMapping("/roles/{roleId}")
    public List<RoleFeaturePermission> byRole(@PathVariable int roleId) {
        return rfpService.findByRoleId(roleId);
    }

    /**
     * GET /api/features/{featureId}/rfp
     * Lấy tất cả RFP cho 1 feature.
     */
    @GetMapping("/features/{featureId}")
    public List<RoleFeaturePermission> byFeature(@PathVariable int featureId) {
        return rfpService.findByFeatureId(featureId);
    }

    /**
     * GET /api/roles/{roleId}/features/{featureId}/rfp
     * Lấy RFP cho cặp role + feature cụ thể.
     */
    @GetMapping("/roles/{roleId}/features/{featureId}")
    public ResponseEntity<RoleFeaturePermission> byRoleAndFeature(@PathVariable int roleId, @PathVariable int featureId) {
        return rfpService.findByRoleAndFeature(roleId, featureId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/roles/{roleId}/features/{featureId}/rfp
     * Tạo hoặc cập nhật bản ghi Role-Feature-Permission (RFP) cho cặp role/feature.
     * Body: RoleFeaturePermission JSON (các flag canCreate/canRead/canUpdate/canDelete)
     */
    @PostMapping("/roles/{roleId}/features/{featureId}")
    public RoleFeaturePermission assignFeatureToRole(@PathVariable int roleId,
                                                     @PathVariable int featureId,
                                                     @RequestBody RoleFeaturePermission rfp) throws Exception {
        var existing = rfpService.findByRoleAndFeature(roleId, featureId);
        if (existing.isPresent()) {
            var ex = existing.get();
            ex.setCanCreate(rfp.getCanCreate());
            ex.setCanRead(rfp.getCanRead());
            ex.setCanUpdate(rfp.getCanUpdate());
            ex.setCanDelete(rfp.getCanDelete());
            return rfpService.update(ex);
        }

        // set references by id
        var role = new com.hrms.model.Role(); role.setRoleId(roleId);
        var feature = new com.hrms.model.Feature(); feature.setFeatureId(featureId);
        rfp.setRole(role);
        rfp.setFeature(feature);
        return rfpService.create(rfp);
    }

    /**
     * DELETE /api/roles/{roleId}/features/{featureId}/rfp
     * Xóa RFP cho cặp role/feature nếu có.
     */
    @DeleteMapping("/roles/{roleId}/features/{featureId}")
    public ResponseEntity<Void> removeRfp(@PathVariable int roleId, @PathVariable int featureId) throws Exception {
        var existing = rfpService.findByRoleAndFeature(roleId, featureId);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        rfpService.delete(existing.get().getId());
        return ResponseEntity.noContent().build();
    }
}
