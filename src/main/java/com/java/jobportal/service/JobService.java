package com.java.jobportal.service;

import com.java.jobportal.form.JobForm;
import com.java.jobportal.model.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface JobService {

    Page<Job> getAllJob(int page, int limit);

    Page<Job> getAllJob(AuthUser authUser, int page, int limit);

    void toggleJobStatus(Long id);

    Page<Job> getApprovedJobs(int page, int limit);

    Page<Job> getJobsByJobType(String jobType,int page, int limit);

    Page<Job> getJobsByCategoryId(Category category, int page, int limit);

    List<Job> getJobsByCompanyId(Company company);

    Page<Job> getJobsByJobTitle(String jobTitle,int page, int limit);

    Page<Job> getJobsByJobTitleAndCategoryId(String jobTitle,Category category,int page,int limit);

    Page<Job> getJobsByJobTitleAndLocationId(String jobTitle, Location location, int page, int limit);

    Page<Job> getJobsByJobTitleAndCategoryIdAndLocationId(String jobTitle,Category category,Location location,int page,int limit);

    Job getJobById(Long id);

    void updateJob(Long id , Status status);

    void deleteJob(Long id);

    void saveJob(Job job);

    void createJob(JobForm jobForm, MultipartFile jobPhotoImg, AuthUser authUser) throws IOException;
}
