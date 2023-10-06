package com.java.jobportal.repository;

import com.java.jobportal.model.PasswordReset;
import com.java.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    
    PasswordReset findByToken(String token);
}
