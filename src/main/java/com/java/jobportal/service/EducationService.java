package com.java.jobportal.service;

import com.java.jobportal.model.Education;
import com.java.jobportal.model.WorkExperience;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationService {
    
    List<Education> getEducationsByUserId(Long id);

    void createEducation(Education education );

    void updateEducation(Education education);

    Education getEducationById(Long id);

    void deleteEducation(Long id);
}
