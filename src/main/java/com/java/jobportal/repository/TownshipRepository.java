package com.java.jobportal.repository;
import com.java.jobportal.model.Location;
import com.java.jobportal.model.Township;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TownshipRepository extends JpaRepository<Township, Long> {
    List<Township> findByLocation(Location location);
}
