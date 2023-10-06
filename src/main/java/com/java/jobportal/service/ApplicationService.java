package com.java.jobportal.service;

import com.java.jobportal.model.Application;
import com.java.jobportal.model.Company;
import com.java.jobportal.model.Job;
import com.java.jobportal.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ApplicationService {

    void saveOrUpdateApplication(Application application);

    Application getById(Long id);

    Page<Application> getByUserId(User user,int page,int limit);

    Page<Application> getByJobId(List<Job> jobs, int page, int limit);

    void deleteById(Long id);
}
