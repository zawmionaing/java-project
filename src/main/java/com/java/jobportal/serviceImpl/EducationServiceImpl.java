package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.Education;
import com.java.jobportal.model.WorkExperience;
import com.java.jobportal.repository.EducationRepository;
import com.java.jobportal.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationServiceImpl implements EducationService {
    @Autowired
    EducationRepository educationRepository;
    
    @Override
    public List<Education> getEducationsByUserId(Long id) {
        
        return educationRepository.findAllByUserId(id);
    }

    @Override
    public void createEducation(Education education) {
        educationRepository.save(education);
    }

    @Override
    public void updateEducation(Education education) {
        educationRepository.save(education);
    }

    @Override
    public Education getEducationById(Long id) {
        return educationRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);

    }


}
