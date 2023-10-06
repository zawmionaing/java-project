package com.java.jobportal.repository;

import com.java.jobportal.model.Application;
import com.java.jobportal.model.Job;
import com.java.jobportal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByUser(User user, Pageable pageable);

    Page<Application> findByJobIdIn(List<Long> jobIds, Pageable pageable);

}
