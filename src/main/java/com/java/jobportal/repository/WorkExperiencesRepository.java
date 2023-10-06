package com.java.jobportal.repository;

import com.java.jobportal.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperiencesRepository extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findAllByUserId(Long id);

}
