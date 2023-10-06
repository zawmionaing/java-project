package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.Category;
import com.java.jobportal.model.WorkExperience;
import com.java.jobportal.repository.WorkExperiencesRepository;
import com.java.jobportal.service.WorkExperiencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkExperiencesServiceImpl implements WorkExperiencesService {
    @Autowired
    WorkExperiencesRepository workExperiencesRepository;

    @Override
    public List<WorkExperience> getExperiencesByUserId(Long id) {
        return workExperiencesRepository.findAllByUserId(id);
    }

    @Override
    public void createExperience(WorkExperience workExperience) {

        workExperiencesRepository.save(workExperience);
    }

    @Override
    public void updateExperience(WorkExperience workExperience) {

        workExperiencesRepository.save(workExperience);
    }

    @Override
    public WorkExperience getExperienceById(Long id) {

        return workExperiencesRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteWorkExperience(Long id)  {


        try {
            workExperiencesRepository.deleteById(id);
        } catch (Exception eee){
            eee.getMessage();
        }
    }


}
