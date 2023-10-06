package com.java.jobportal.model;


import com.java.jobportal.form.UserProfileForm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String cvForm;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private String nrc;

    @Column
    private String address;

    @Column
    private String photo;

    @Column
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserProfile(String cvForm, LocalDate dateOfBirth, String nrc, String address, String photo, String phone) {
        this.cvForm = cvForm;
        this.dateOfBirth = dateOfBirth;
        this.nrc = nrc;
        this.address = address;
        this.photo = photo;
        this.phone = phone;
    }

    public UserProfile() {
    }

    public UserProfile(UserProfileForm userProfileForm) {
        this.cvForm = userProfileForm.getCvForm();
        this.dateOfBirth = userProfileForm.getDateOfBirth();
        this.nrc = userProfileForm.getNrc();
        this.address = userProfileForm.getAddress();
        this.photo = userProfileForm.getPhoto();
        this.phone = userProfileForm.getPhone();
    }
}