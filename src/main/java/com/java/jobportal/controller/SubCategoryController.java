package com.java.jobportal.controller;

import com.java.jobportal.model.Category;
import com.java.jobportal.model.SubCategory;
import com.java.jobportal.service.CategoryService;
import com.java.jobportal.serviceImpl.SubCategoryServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/subcategories")
public class SubCategoryController {
    @Autowired
    SubCategoryServiceImp subCategoryServiceImp;

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = {"/list", "/"}, method = RequestMethod.GET)
    public ModelAndView showSubCategoryList(
            @RequestParam(name ="page", required = false, defaultValue = "0") int page
    ) {
        ModelAndView modelAndView ;
        Page<SubCategory> subCategoryPage = subCategoryServiceImp.getAllSubCategories(page,5);
        List<SubCategory> subCategoryList = subCategoryPage.getContent();
        if(page>0 && subCategoryList.isEmpty()){
           modelAndView = new ModelAndView("redirect:/admin/subcategories/list?page="+(subCategoryPage.getTotalPages() -1));
        }else{
            modelAndView = new ModelAndView("admin/subcategory/subCategoryList");
        }
        modelAndView.addObject("subCategoryList", subCategoryList);
        modelAndView.getModelMap().addAttribute("totalPages",subCategoryPage.getTotalPages() - 1);
        modelAndView.getModelMap().addAttribute("currentPage",subCategoryPage.getNumber());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createSubCategory(@RequestParam(name = "page", required = false, defaultValue = "0") int currentPage) {
        ModelAndView modelAndView = new ModelAndView("admin/subcategory/upsertSubCategory");
        List<Category> categories = categoryService.getAllCategories();
        modelAndView.addObject("subCategory", new SubCategory());
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("currentPage", currentPage);
        return modelAndView;
    }

    @PostMapping("/upsert")
    public ModelAndView saveSubCategory(@ModelAttribute("subCategory") @Valid SubCategory subCategory, BindingResult bindingResult,
                                        @RequestParam(name = "currentPage", required = false, defaultValue = "0") int currentPage, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (subCategoryServiceImp.isSubCategoryNameUnique(subCategory.getName(), subCategory.getCategory().getId())) {
            bindingResult.rejectValue("name", "error.subCategory", "Subcategory with this name already exists");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("categories", categoryService.getAllCategories());
            modelAndView.addObject("currentPage",currentPage);
            modelAndView.setViewName("admin/subcategory/upsertSubCategory");
        } else {
            subCategoryServiceImp.createCategory(subCategory);
            redirectAttributes.addFlashAttribute("success", true);
            modelAndView.setViewName("redirect:/admin/subcategories/list?page=" + currentPage);
        }
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editSubCategory(@PathVariable Long id, @RequestParam(name = "page", required = false, defaultValue = "0") int currentPage) {
        ModelAndView modelAndView = new ModelAndView("admin/subcategory/upsertSubCategory");
        List<Category> categories = categoryService.getAllCategories();
        SubCategory subCategory = subCategoryServiceImp.getCategoryById(id);
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("subCategory", subCategory);
        modelAndView.addObject("currentPage", currentPage); // Pass current page to the view
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteSubCategory(@PathVariable Long id, @RequestParam(name = "page", required = false, defaultValue = "0") int currentPage, RedirectAttributes redirectAttributes) {
        Long jobCount = subCategoryServiceImp.deleteCategory(id);
        if (jobCount > 0 ){
            redirectAttributes.addFlashAttribute("jobCount",jobCount);
        }else{
            redirectAttributes.addFlashAttribute("delete", true);
        }
        return new ModelAndView("redirect:/admin/subcategories/list?page=" + currentPage);
    }
}