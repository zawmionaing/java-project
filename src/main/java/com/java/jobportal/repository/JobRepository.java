package com.java.jobportal.repository;

import com.java.jobportal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findAll(Pageable pageable);

    Page<Job> findAllByCompanyId(long CompanyID,Pageable pageable);

    Page<Job> findByStatus(Status status,Pageable pageable);

    Page<Job> findByJobTypeAndStatus(JobType jobType,Status status,Pageable pageable);

    List<Job> findBySubCategoryAndStatus(SubCategory subCategory, Status status);

    Page<Job> findByJobTitleContainingIgnoreCaseAndStatus(String jobTitle,Status status,Pageable pageable);

    List<Job> findByJobTitleContainingIgnoreCaseAndSubCategoryAndStatus(String jobTitle,SubCategory subCategory,Status status);

    List<Job> findByJobTitleContainingIgnoreCaseAndTownshipAndStatus(String jobTitle,Township township,Status status);

    List<Job> findByJobTitleContainingIgnoreCaseAndSubCategoryAndTownshipAndStatus(String jobTitle,SubCategory subCategory,Township township,Status status);

    @Modifying
    @Query("update Job set status=:status where id=:id")
    void updateJob(@Param("id") Long id, @Param("status") Status status);

    Long countBySubCategory_Id(Long id);
}
