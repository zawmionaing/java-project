package com.java.jobportal.repository;

import com.java.jobportal.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findAll(Pageable pageable);
    Company findByName(String name);
}
