package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.SubCategory;
import com.java.jobportal.repository.JobRepository;
import com.java.jobportal.repository.SubCategoryRepository;
import com.java.jobportal.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SubCategoryServiceImp implements SubCategoryService {
    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    JobRepository jobRepository;

    @Override
    public Page<SubCategory> getAllSubCategories(int page, int limit) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Pageable pageable= PageRequest.of(page,limit,Sort.by(order));
        return subCategoryRepository.findAll(pageable);
    }

    @Override
    public SubCategory getCategoryById(Long id) {
        return subCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public void createCategory(SubCategory subCategory) {
        subCategoryRepository.save(subCategory);
    }

    @Override
    public void updateCategory(SubCategory subCategory) {
        subCategoryRepository.save(subCategory);
    }

    @Override
    public Long deleteCategory(Long id) {
        Long jobCount = jobRepository.countBySubCategory_Id(id);
        if (jobCount > 0){
            return jobCount;
        }else {
            subCategoryRepository.deleteById(id);
        }
        return 0l;
    }

    public boolean isSubCategoryNameUnique(String name,Long id) {
        SubCategory existingCategory = subCategoryRepository.findByNameAndCategoryId(name,id);
        return existingCategory != null;
    }
}



