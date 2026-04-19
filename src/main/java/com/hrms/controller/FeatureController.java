package com.hrms.controller;

import com.hrms.model.Feature;
import com.hrms.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/features")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    /**
     * GET /api/features
     * Trả về danh sách tất cả các feature (chức năng) đã đăng ký trong hệ thống.
     * Dùng để quản trị xem những chức năng nào có thể gán permission.
     */
    @GetMapping("")
    public List<Feature> getFeatures() {
        return featureService.fetchFeatures();
    }

    /**
     * POST /api/features
     * Tạo mới một feature (tên chức năng, endpoint, mô tả).
     * Body: Feature JSON
     */
    @PostMapping("")
    public Feature createFeature(@RequestBody Feature feature) {
        return featureService.register(feature);
    }

    /**
     * GET /api/features/{id}
     * Lấy thông tin chi tiết của feature theo id.
     */
    @GetMapping("/{id}")
    public Feature getFeature(@PathVariable int id) throws Exception {
        return featureService.getById(id).orElseThrow(() -> new Exception("Feature not found"));
    }

    /**
     * PUT /api/features/{id}
     * Cập nhật thông tin feature (tên, endpoint, mô tả).
     * Body: Feature JSON
     */
    @PutMapping("/{id}")
    public Feature updateFeature(@PathVariable int id, @RequestBody Feature feature) throws Exception {
        Feature existing = featureService.getById(id).orElseThrow(() -> new Exception("Feature not found"));
        existing.setFeatureName(feature.getFeatureName());
        existing.setEndpoint(feature.getEndpoint());
        existing.setDescription(feature.getDescription());
        return featureService.editFeature(existing);
    }

    /**
     * DELETE /api/features/{id}
     * Xóa feature theo id (thực tế xóa hoặc đánh dấu is_deleted tuỳ implementation DB).
     */
    @DeleteMapping("/{id}")
    public void deleteFeature(@PathVariable int id) {
        featureService.deleteFeature(id);
    }
}
