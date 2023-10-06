package com.java.jobportal.service;

import com.java.jobportal.model.SubCategory;
import org.springframework.data.domain.Page;


public interface SubCategoryService {

    Page<SubCategory> getAllSubCategories(int page, int limit);

    SubCategory getCategoryById(Long id);

    void createCategory(SubCategory subCategory);

    void updateCategory(SubCategory subCategory);

    Long deleteCategory(Long id);
}
