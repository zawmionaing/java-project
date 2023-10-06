package com.java.jobportal.repository;

import com.java.jobportal.model.Category;
import com.java.jobportal.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    Long countByCategoryId(Long id);

    List<SubCategory> findByCategory(Category category);

    SubCategory findByNameAndCategoryId(String name,Long id);
}
