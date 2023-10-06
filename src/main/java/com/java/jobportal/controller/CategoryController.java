package com.java.jobportal.controller;

import com.java.jobportal.model.Category;
import com.java.jobportal.serviceImpl.CategoryServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryServiceImp categoryServiceImp;
    @RequestMapping(value = {"/list", "/"}, method = RequestMethod.GET)
    public ModelAndView showCategoryList(
            @RequestParam(name ="page", required = false, defaultValue = "0") int page
    ) {
        ModelAndView modelAndView ;
        Page<Category> categoryPage= categoryServiceImp.getAllCategory(page,5);
        List <Category> categoryList=categoryPage.getContent();
        if(page>0 && categoryList.isEmpty()){
            modelAndView = new ModelAndView("redirect:/admin/categories/list?page="+(categoryPage.getTotalPages() -1));
        }else{
            modelAndView = new ModelAndView("admin/category/categoryList");
        }
        modelAndView.addObject("categoryList", categoryList);
        modelAndView.getModelMap().addAttribute("totalPages", categoryPage.getTotalPages() - 1);
        modelAndView.getModelMap().addAttribute("currentPage", categoryPage.getNumber());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createCategory(@RequestParam(name = "page", required = false, defaultValue = "0") int currentPage) {
        ModelAndView modelAndView = new ModelAndView("admin/category/upsertCategory");
        modelAndView.addObject("category", new Category());
        modelAndView.addObject("currentPage",currentPage);
        return modelAndView;
    }

    @PostMapping("/upsert")
    public String saveCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam(name = "currentPage", required = false, defaultValue = "0") int page) {
        if (categoryServiceImp.isCategoryNameUnique(category.getName())) {
            bindingResult.rejectValue("name", "error.category", "Category with this name already exists");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentPage",page);
            return "admin/category/upsertCategory";
        } else {
            categoryServiceImp.createCategory(category);
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/admin/categories/list?page=" + page;
        }
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editCategory(@PathVariable Long id, @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("admin/category/upsertCategory");
        Category category = categoryServiceImp.getCategoryById(id);
        modelAndView.addObject("category", category);
        modelAndView.addObject("currentPage", page);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, @RequestParam(name = "page", required = false, defaultValue = "0") int page, RedirectAttributes redirectAttributes) {
        Long subCategoryCount = categoryServiceImp.deleteCategory(id);
        if (subCategoryCount != null){
            redirectAttributes.addFlashAttribute("subCategoryCount",subCategoryCount);
        }else{
            redirectAttributes.addFlashAttribute("delete", true);
        }
        return "redirect:/admin/categories/list?page=" + page;
    }
}

