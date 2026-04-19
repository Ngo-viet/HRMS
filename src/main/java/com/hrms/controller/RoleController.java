package com.hrms.controller;

import com.hrms.model.Role;
import com.hrms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * GET /api/roles
     * Trả về danh sách tất cả vai trò.
     */
    @GetMapping("")
    public List<Role> getRoles() {
        return roleService.fetchRoles();
    }

    /**
     * POST /api/roles
     * Tạo vai trò mới. Body: Role JSON (roleName, description).
     */
    @PostMapping("")
    public Role createRole(@RequestBody Role role) throws Exception {
        if (role.getRoleName() != null) {
            if (roleService.findByName(role.getRoleName()).isPresent()) {
                throw new Exception("Role with name " + role.getRoleName() + " already exists");
            }
        }
        return roleService.register(role);
    }

    /**
     * GET /api/roles/{id}
     * Lấy thông tin vai trò theo id.
     */
    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable int id) throws Exception {
        return roleService.getById(id).orElseThrow(() -> new Exception("Role not found"));
    }

    /**
     * PUT /api/roles/{id}
     * Cập nhật vai trò theo id. Body: Role JSON
     */
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable int id, @RequestBody Role role) throws Exception {
        Role existing = roleService.getById(id).orElseThrow(() -> new Exception("Role not found"));
        existing.setRoleName(role.getRoleName());
        existing.setDescription(role.getDescription());
        return roleService.editRole(existing);
    }

    /**
     * DELETE /api/roles/{id}
     * Xóa vai trò theo id.
     */
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
    }
}
