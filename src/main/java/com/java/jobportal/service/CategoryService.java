package com.java.jobportal.service;

import com.java.jobportal.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    Page<Category> getAllCategory(int page, int limit);

    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    void createCategory(Category category);

    void updateCategory(Category category);

    Long deleteCategory(Long id);

    boolean isCategoryNameUnique(String name);
}
