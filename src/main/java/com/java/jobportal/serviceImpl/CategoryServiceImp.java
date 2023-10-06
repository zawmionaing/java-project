package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.Category;
import com.java.jobportal.repository.CategoryRepository;
import com.java.jobportal.repository.SubCategoryRepository;
import com.java.jobportal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllCategory(int page , int limit) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Pageable pageable= PageRequest.of(page,limit,Sort.by(order));
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Long deleteCategory(Long id) {
        Long subCategoryCount = subCategoryRepository.countByCategoryId(id);
        if (subCategoryCount > 0) {
            return subCategoryCount;
        } else {
            categoryRepository.deleteById(id);
        }
        return null;
    }

    @Override
    public boolean isCategoryNameUnique(String name) {
        Category existingCategory = categoryRepository.findByName(name);
        return existingCategory != null;
    }
}


