package com.java.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "townships")
public class Township {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "township",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Job> job;

    public Township(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Township() {
    }
}