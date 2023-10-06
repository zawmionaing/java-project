package com.java.jobportal.repository;

import com.java.jobportal.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    
    List<Education> findAllByUserId(Long id);
}
