package com.hrms.service;

import com.hrms.model.Feature;
import com.hrms.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    public List<Feature> fetchFeatures() {
        return featureRepository.findAll();
    }

    public Feature register(Feature f) {
        return featureRepository.save(f);
    }

    public Optional<Feature> getById(int id) {
        return featureRepository.findById(id);
    }

    public Feature editFeature(Feature f) {
        return featureRepository.save(f);
    }

    public void deleteFeature(int id) {
        featureRepository.deleteById(id);
    }
}
