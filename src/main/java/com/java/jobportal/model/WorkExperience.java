package com.java.jobportal.model;


import com.java.jobportal.form.WorkExperienceForm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "work_experiences")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column
    private String companyName;

    @Column
    private String jobTitle;

    @Column
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public WorkExperience(WorkExperienceForm workExperienceForm) {
        this.id=workExperienceForm.getId();
        this.user=workExperienceForm.getUser();
        this.startDate = workExperienceForm.getStartDate();
        this.endDate = workExperienceForm.getEndDate();
        this.companyName = workExperienceForm.getCompanyName();
        this.jobTitle = workExperienceForm.getJobTitle();
        this.description = workExperienceForm.getDescription();
    }
    public WorkExperience() {
    
    }

}