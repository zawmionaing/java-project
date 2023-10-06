package com.java.jobportal.form;

import com.java.jobportal.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
public class WorkExperienceForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @NotEmpty(message = "Please pickup a start date")
    private String startDate;
    
    private String endDate;
    
    @NotBlank(message = "Please fill the field")
    @NotEmpty(message = "Company name shouldn't be empty")
    private String companyName;
    
    @NotBlank(message = "Please fill the field")
    @NotEmpty(message = "Job title name shouldn't be empty")
    private String jobTitle;
    
    private String description;
}
