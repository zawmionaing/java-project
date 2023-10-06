
package com.java.jobportal.model;

import com.java.jobportal.form.CompanyRegisterForm;
import com.java.jobportal.form.UserRegisterForm;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false)
    @NotEmpty(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Enter valid email")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column
    private boolean disable = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @EqualsAndHashCode.Exclude
    @OneToOne(optional = false, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private Company company;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PasswordReset> passwordReset;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Application> applications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Education> educations;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.disable = false;
    }

    public User(UserRegisterForm userform) {
        name = userform.getName();
        email = userform.getEmail();
        password = userform.getPassword();
    }

    public User(CompanyRegisterForm companyRegisterForm) {
        name = companyRegisterForm.getName();
        email = companyRegisterForm.getEmail();
        password = companyRegisterForm.getPassword();
    }
}