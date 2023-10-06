package com.java.jobportal.controller;

import com.java.jobportal.model.*;
import com.java.jobportal.repository.*;
import com.java.jobportal.service.AdminService;
import com.java.jobportal.service.TownshipService;
import com.java.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    TownshipRepository townshipRepository;

    @Autowired
    UserService userService;


    public Long getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String currentLoginName = authentication.getName();
        return userService.getUserByName(currentLoginName).getId();
    }
    @GetMapping(value = {"/","/dashboard"})
    public String Dashboard(Model model) throws  Exception{
        List<User> users = userRepository.findAll();
        List<Company> companies = companyRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        List<Job> jobs = jobRepository.findAll();
        List<Township> townships = townshipRepository.findAll();
        User authUser = userService.getById(getAuthenticatedUser());
        model.addAttribute("authUser", authUser);
        model.addAttribute("userCount", users.size());
        model.addAttribute("companyCount", companies.size());
        model.addAttribute("categoryCount", categories.size());
        model.addAttribute("subCategoryCount", subCategories.size());
        model.addAttribute("jobCount", jobs.size());
        model.addAttribute("townshipCount", townships.size());
        return "/admin/dashboard";
    }

}
