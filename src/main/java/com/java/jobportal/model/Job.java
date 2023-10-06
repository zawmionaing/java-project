package com.java.jobportal.model;

import com.java.jobportal.form.JobForm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "jobs")
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jobTitle;

    @Column
    private String jobPhoto;

    @Column
    private String shortDescription;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String requirement;

    @Column
    private String salary;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Application> applications;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "township_id")
    private Township township;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    public Job(JobForm jobForm){
        id = jobForm.getId();
        jobTitle=jobForm.getJobTitle();
        jobPhoto=jobForm.getJobPhoto();
        requirement = jobForm.getRequirement();
        township = jobForm.getTownship();
        subCategory = jobForm.getSubCategory();
        shortDescription = jobForm.getShortDescription();
        description = jobForm.getDescription();
        salary = jobForm.getSalary();
        jobType = jobForm.getJobType();
    }

}