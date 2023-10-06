package com.java.jobportal.service;

import com.java.jobportal.model.Category;
import com.java.jobportal.model.Education;
import com.java.jobportal.model.WorkExperience;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkExperiencesService {
    List<WorkExperience> getExperiencesByUserId(Long id);

    void createExperience(WorkExperience workExperience);
    void updateExperience(WorkExperience workExperience);

    WorkExperience getExperienceById(Long id);

    void deleteWorkExperience(Long id);
}
