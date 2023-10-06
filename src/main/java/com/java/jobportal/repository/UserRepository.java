package com.java.jobportal.repository;

import com.java.jobportal.model.Role;
import com.java.jobportal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByNameOrEmail(String username, String email);

    User findByName(String name);

    Page <User> findByRoleNot(Role role,Pageable pageable);

    boolean existsByName(String username);

    User findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.disable = :disable WHERE u.id = :id")
    void updateUserIsDisable(@Param("id")Long id,@Param("disable") boolean disable);
}
