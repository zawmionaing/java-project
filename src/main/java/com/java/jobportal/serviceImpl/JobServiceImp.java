package com.java.jobportal.serviceImpl;

import com.java.jobportal.form.JobForm;
import com.java.jobportal.model.*;
import com.java.jobportal.repository.JobRepository;
import com.java.jobportal.repository.SubCategoryRepository;
import com.java.jobportal.repository.TownshipRepository;
import com.java.jobportal.service.JobService;
import com.java.jobportal.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class JobServiceImp implements JobService {

    private final String UPLOAD_DIRECTORY_JOB = "src/main/resources/static/job/img/";

    @Autowired
    JobRepository jobRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    TownshipRepository townshipRepository;

    @Autowired
    UserService userService;

    @Override
    public Page<Job> getAllJob(int page,int limit) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "jobTitle");
        Pageable pageable= PageRequest.of(page,limit,Sort.by(order));
        return jobRepository.findAll(pageable);
    }

    @Override
    public Page<Job> getAllJob(AuthUser authUser, int page, int limit) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "jobTitle");
        Pageable pageable= PageRequest.of(page,limit,Sort.by(order));
        User user = userService.getByUserNameOrEmail(authUser.getUsername(),authUser.getUsername());
        Long companyId = user.getCompany().getId();
        return jobRepository.findAllByCompanyId(companyId,pageable);
    }

    @Override
    public Job getJobById(Long id) {
        return  jobRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateJob(Long id , Status status) {
       jobRepository.updateJob(id ,status);
    }

    @Override
    public void deleteJob(Long id) {
         jobRepository.deleteById(id);
    }

    @Override
    public void saveJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public void toggleJobStatus(Long id) {
        Job job = getJobById(id);
        if (job != null) {
            if (job.getStatus() == Status.APPROVED) {
                job.setStatus(Status.REJECTED);
            } else {
                job.setStatus(Status.APPROVED);
            }
            jobRepository.save(job);
        }
    }

    @Override
    public Page<Job> getApprovedJobs(int page,int limit){
        Pageable pageable = PageRequest.of(page, limit);
        return jobRepository.findByStatus(Status.APPROVED,pageable);
    }

    @Override
    public Page<Job> getJobsByJobType(String jobType, int page, int limit) {
        JobType jobType1 = jobType.equals("Full_Time") ? JobType.Full_Time : JobType.Part_Time;
        Sort sort = Sort.by(Sort.Order.asc("jobTitle"));
        Pageable pageable = PageRequest.of(page, limit,sort);
        return jobRepository.findByJobTypeAndStatus(jobType1,Status.APPROVED,pageable);
    }

    @Override
    @Transactional
    public Page<Job> getJobsByCategoryId(Category category, int page, int limit) {
        List<SubCategory> subCategories = subCategoryRepository.findByCategory(category);
        List<Job> jobs = new ArrayList<Job>();
        for(SubCategory subCategory : subCategories) {
            jobs.addAll(jobRepository.findBySubCategoryAndStatus(subCategory,Status.APPROVED));
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Job> jobPage = new PageImpl<>(jobs,pageable,jobs.size());
        return jobPage;
    }

    @Override
    public List<Job> getJobsByCompanyId(Company company){
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Job> jobs = jobRepository.findAllByCompanyId(company.getId(),pageable);
        return jobs.getContent();
    }

    @Override
    public Page<Job> getJobsByJobTitle(String jobTitle, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return jobRepository.findByJobTitleContainingIgnoreCaseAndStatus(jobTitle,Status.APPROVED,pageable);
    }

    @Override
    public Page<Job> getJobsByJobTitleAndCategoryId(String jobTitle,Category category, int page, int limit) {
        List<SubCategory> subCategories = subCategoryRepository.findByCategory(category);
        List<Job> jobs = new ArrayList<Job>();
        for(SubCategory subCategory : subCategories) {
            jobs.addAll(jobRepository.findByJobTitleContainingIgnoreCaseAndSubCategoryAndStatus(jobTitle,subCategory,Status.APPROVED));
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Job> jobPage = new PageImpl<>(jobs,pageable,jobs.size());
        return jobPage;
    }

    @Override
    public Page<Job> getJobsByJobTitleAndLocationId(String jobTitle, Location location, int page, int limit) {
        List<Township> townships = townshipRepository.findByLocation(location);
        List<Job> jobs = new ArrayList<Job>();
        for(Township township : townships) {
            jobs.addAll(jobRepository.findByJobTitleContainingIgnoreCaseAndTownshipAndStatus(jobTitle,township,Status.APPROVED));
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Job> jobPage = new PageImpl<>(jobs,pageable,jobs.size());
        return jobPage;
    }

    @Override
    public Page<Job> getJobsByJobTitleAndCategoryIdAndLocationId(String jobTitle, Category category, Location location, int page, int limit) {
        List<SubCategory> subCategories = subCategoryRepository.findByCategory(category);
        List<Township> townships = townshipRepository.findByLocation(location);
        List<Job> jobs = new ArrayList<Job>();
        for(SubCategory subCategory : subCategories) {
            for(Township township : townships){
                jobs.addAll(jobRepository.findByJobTitleContainingIgnoreCaseAndSubCategoryAndTownshipAndStatus(jobTitle,subCategory,township,Status.APPROVED));
            }
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Job> jobPage = new PageImpl<>(jobs,pageable,jobs.size());
        return jobPage;
    }

    @Override
    public void createJob(JobForm jobForm, MultipartFile jobPhotoImg, AuthUser authUser) throws IOException {
        Job job = new Job(jobForm);
        User user = userService.getByUserNameOrEmail(authUser.getUsername(),authUser.getUsername());
        if (jobPhotoImg != null && !jobPhotoImg.isEmpty()) {
            String jobPhotoFileName = saveFile(jobPhotoImg, UPLOAD_DIRECTORY_JOB);
            job.setJobPhoto(jobPhotoFileName);

            if (job.getId()!=null){
                Job existingJob = jobRepository.getReferenceById(job.getId());
                if (existingJob != null && !Objects.equals(existingJob.getJobPhoto(), jobPhotoFileName)) {
                    deleteFile(existingJob.getJobPhoto(), UPLOAD_DIRECTORY_JOB);
                }
            }
        }
        job.setCompany(user.getCompany());
        jobRepository.save(job);
    }

    private String saveFile(MultipartFile file,String uploadDirectory) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filePath = uploadDirectory + fileName;
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        String targetDirectory = "target/classes/static/job/img/";
        String targetFilePath = targetDirectory + fileName;
        Files.copy(file.getInputStream(), Paths.get(targetFilePath), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private void deleteFile(String filePath,String uploadDirectory) {
        if (StringUtils.isNotBlank(filePath)) {
            File fileToDelete = new File(uploadDirectory + filePath);
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                fileToDelete.delete();
            }
        }
    }
}
