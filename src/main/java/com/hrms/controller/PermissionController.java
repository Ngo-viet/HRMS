package com.hrms.controller;

import com.hrms.model.Permission;
import com.hrms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * GET /api/permissions
     * Trả về danh sách tất cả permission (Role-Feature với các flag CRUD).
     */
    @GetMapping("")
    public List<Permission> getPermissions() {
        return permissionService.fetchPermissions();
    }

    /**
     * POST /api/permissions
     * Tạo permission mới (body: Permission JSON với role và feature reference và các flag).
     */
    @PostMapping("")
    public Permission createPermission(@RequestBody Permission p) {
        return permissionService.register(p);
    }

    /**
     * GET /api/permissions/{id}
     * Lấy permission theo id.
     */
    @GetMapping("/{id}")
    public Permission getPermission(@PathVariable int id) throws Exception {
        return permissionService.getById(id).orElseThrow(() -> new Exception("Permission not found"));
    }

    /**
     * PUT /api/permissions/{id}
     * Cập nhật flags của permission (canCreate/canRead/canUpdate/canDelete).
     */
    @PutMapping("/{id}")
    public Permission updatePermission(@PathVariable int id, @RequestBody Permission p) throws Exception {
        Permission existing = permissionService.getById(id).orElseThrow(() -> new Exception("Permission not found"));
        existing.setCanCreate(p.getCanCreate());
        existing.setCanRead(p.getCanRead());
        existing.setCanUpdate(p.getCanUpdate());
        existing.setCanDelete(p.getCanDelete());
        return permissionService.editPermission(existing);
    }

    /**
     * DELETE /api/permissions/{id}
     * Xóa permission theo id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable int id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/roles/{roleId}/permissions
     * Lấy danh sách permission cho một role cụ thể.
     */
    @GetMapping("/roles/{roleId}")
    public List<Permission> getPermissionsByRole(@PathVariable int roleId) {
        return permissionService.getByRole(roleId);
    }

    /**
     * GET /api/features/{featureId}/permissions
     * Lấy danh sách permission cho một feature cụ thể.
     */
    @GetMapping("/features/{featureId}")
    public List<Permission> getPermissionsByFeature(@PathVariable int featureId) {
        return permissionService.getByFeature(featureId);
    }

    /**
     * POST /api/roles/{roleId}/features/{featureId}/permissions
     * Gán permission cho cặp (role, feature). Nếu permission đã tồn tại sẽ cập nhật các flag,
     * nếu chưa tồn tại sẽ tạo mới.
     * Body: Permission JSON (các flag canCreate/canRead/canUpdate/canDelete) — role và feature được lấy từ path.
     */
    @PostMapping("/roles/{roleId}/features/{featureId}")
    public Permission assignPermissionToFeature(@PathVariable int roleId,
                                                @PathVariable int featureId,
                                                @RequestBody Permission p) throws Exception {
        // tìm permission hiện có
        var existing = permissionService.getByRoleAndFeature(roleId, featureId);
        if (existing.isPresent()) {
            Permission ex = existing.get();
            ex.setCanCreate(p.getCanCreate());
            ex.setCanRead(p.getCanRead());
            ex.setCanUpdate(p.getCanUpdate());
            ex.setCanDelete(p.getCanDelete());
            return permissionService.editPermission(ex);
        }

        // tạo mới: set role & feature reference (chỉ id cần thiết)
        var role = new com.hrms.model.Role();
        role.setRoleId(roleId);
        var feature = new com.hrms.model.Feature();
        feature.setFeatureId(featureId);
        p.setRole(role);
        p.setFeature(feature);
        return permissionService.register(p);
    }

    /**
     * DELETE /api/roles/{roleId}/features/{featureId}/permissions
     * Xóa permission cho cặp (role, feature) nếu tồn tại.
     */
    @DeleteMapping("/roles/{roleId}/features/{featureId}")
    public ResponseEntity<Void> removePermissionFromFeature(@PathVariable int roleId,
                                                            @PathVariable int featureId) throws Exception {
        var existing = permissionService.getByRoleAndFeature(roleId, featureId);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        permissionService.deletePermission(existing.get().getPermissionId());
        return ResponseEntity.noContent().build();
    }
}
