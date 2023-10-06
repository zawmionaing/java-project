package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.Application;
import com.java.jobportal.model.Company;
import com.java.jobportal.model.Job;
import com.java.jobportal.model.User;
import com.java.jobportal.repository.ApplicationRepository;
import com.java.jobportal.service.ApplicationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Override
    public void saveOrUpdateApplication(Application application) {
        applicationRepository.saveAndFlush(application);
    }

    @Override
    public Application getById(Long id) {
        return applicationRepository.findById(id).get();
    }

    @Override
    public Page<Application> getByUserId(User user,int page, int limit) {
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable= PageRequest.of(page,limit,sort);
        Page<Application> applications = applicationRepository.findByUser(user,pageable);
        return applications;
    }

    @Override
    public Page<Application> getByJobId(List<Job> jobs, int page, int size) {
        List<Long> jobIds = jobs.stream().map(Job::getId).collect(Collectors.toList());
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return applicationRepository.findByJobIdIn(jobIds, PageRequest.of(page, size, sort));
    }

    @Override
    public void deleteById(Long id) {
        applicationRepository.deleteById(id);
    }
}
