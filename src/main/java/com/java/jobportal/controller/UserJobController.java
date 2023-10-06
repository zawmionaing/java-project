package com.java.jobportal.controller;

import com.java.jobportal.model.*;
import com.java.jobportal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserJobController {

    @Autowired
    JobService jobService;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    LocationService locationService;

    @Autowired
    ApplicationService applicationService;

    public Long getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String currentLoginName = authentication.getName();
        return userService.getUserByName(currentLoginName).getId();
    }

    @RequestMapping(value = "/jobs",method = RequestMethod.GET)
    public ModelAndView directjobPage(
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "jobtype",required = false,defaultValue = "") String jobType,
            @RequestParam(name = "categoryKey",required = false) Long categoryKey,
            @RequestParam(name = "categoryId",required = false) Long categoryId,
            @RequestParam(name = "jobTitle",required = false,defaultValue = "") String jobTitle,
            @RequestParam(name = "locationId",required = false)Long locationId
    ) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Page<Job> jobsPage = null;
        List<Job> jobs = new ArrayList<Job>();
        if(getAuthenticatedUser() != null){
            User authUser = userService.getById(getAuthenticatedUser());
            List<Application> applications = applicationService.getByUserId(authUser,0,Integer.MAX_VALUE).getContent();
            List<Long> jobIdFromApp = applications.stream().map(Application::getJob).map(Job::getId).toList();
            modelAndView.getModelMap().addAttribute("jobIdList", jobIdFromApp);
            if(authUser.getUserProfile() == null){
                modelAndView.addObject("authUserProfile",new UserProfile());
            }else{
                modelAndView.addObject("authUserProfile",authUser.getUserProfile());
            }
        }else {
            modelAndView.addObject("authUserProfile",new UserProfile());
            modelAndView.getModelMap().addAttribute("jobIdList", new ArrayList<Long>());
        }
        if(!jobType.equals("")){
            jobsPage = jobService.getJobsByJobType(jobType,page,5);
            modelAndView.getModelMap().addAttribute("category","");
            modelAndView.getModelMap().addAttribute("location","");
            modelAndView.getModelMap().addAttribute("categoryKey","");
        } else if (categoryKey != null) {
            Category category = categoryService.getCategoryById(categoryKey);
            jobsPage = jobService.getJobsByCategoryId(category,page,5);
            modelAndView.getModelMap().addAttribute("category","");
            modelAndView.getModelMap().addAttribute("location","");
            modelAndView.getModelMap().addAttribute("categoryKey",category);
        }else if(!jobTitle.equals("")){
            if(categoryId != null && locationId == null){
                Category category = categoryService.getCategoryById(categoryId);
                jobsPage = jobService.getJobsByJobTitleAndCategoryId(jobTitle,category,page,5);
                modelAndView.getModelMap().addAttribute("category",category);
                modelAndView.getModelMap().addAttribute("location","");
            }else if(categoryId == null && locationId != null){
                Location location = locationService.getLocationById(locationId);
                jobsPage = jobService.getJobsByJobTitleAndLocationId(jobTitle,location,page,5);
                modelAndView.getModelMap().addAttribute("category","");
                modelAndView.getModelMap().addAttribute("location",location);
            }else if(categoryId != null && locationId != null){
                Category category = categoryService.getCategoryById(categoryId);
                Location location = locationService.getLocationById(locationId);
                jobsPage = jobService.getJobsByJobTitleAndCategoryIdAndLocationId(jobTitle,category,location,page,5);
                modelAndView.getModelMap().addAttribute("category",category);
                modelAndView.getModelMap().addAttribute("location",location);
            }else{
                jobsPage = jobService.getJobsByJobTitle(jobTitle,page,5);
                modelAndView.getModelMap().addAttribute("category","");
                modelAndView.getModelMap().addAttribute("location","");
            }
            modelAndView.getModelMap().addAttribute("categoryKey","");
        }else{
            jobsPage = jobService.getApprovedJobs(page,5);
            modelAndView.getModelMap().addAttribute("category","");
            modelAndView.getModelMap().addAttribute("location","");
            modelAndView.getModelMap().addAttribute("categoryKey","");
        }
        jobs = jobsPage.getContent();
        modelAndView.addObject("jobs",jobs);
        modelAndView.getModelMap().addAttribute("categoryIdKey",categoryKey);
        modelAndView.getModelMap().addAttribute("categoryId",categoryId);
        modelAndView.getModelMap().addAttribute("locationId",locationId);
        modelAndView.getModelMap().addAttribute("jobType",jobType);
        modelAndView.getModelMap().addAttribute("jobTitle",jobTitle);
        modelAndView.getModelMap().addAttribute("totalPages", jobsPage.getTotalPages() - 1);
        modelAndView.getModelMap().addAttribute("currentPage", jobsPage.getNumber());
        modelAndView.setViewName("jobs");
        return modelAndView;
    }

    @RequestMapping(value = "/jobs/jobDetail/{id}",method = RequestMethod.GET)
    public ModelAndView directJobDetailPage(
            @PathVariable Long id,
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "jobtype",required = false,defaultValue = "") String jobType,
            @RequestParam(name = "categoryKey",required = false) Long categoryKey,
            @RequestParam(name = "categoryId",required = false) Long categoryId,
            @RequestParam(name = "jobTitle",required = false,defaultValue = "") String jobTitle,
            @RequestParam(name = "locationId",required = false)Long locationId,
            @RequestParam(name = "apply",required = false) Long apply
    ) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        if(getAuthenticatedUser() != null){
            User authUser = userService.getById(getAuthenticatedUser());
            List<Application> applications = applicationService.getByUserId(authUser,0,Integer.MAX_VALUE).getContent();
            List<Long> jobIdFromApp = applications.stream().map(Application::getJob).map(Job::getId).toList();
            modelAndView.getModelMap().addAttribute("jobIdList", jobIdFromApp);
        }else {
            modelAndView.getModelMap().addAttribute("jobIdList", new ArrayList<Long>());
        }
        Job job = jobService.getJobById(id);
        modelAndView.addObject("job",job);
        modelAndView.getModelMap().addAttribute("currentPage",page);
        modelAndView.getModelMap().addAttribute("jobType",jobType);
        modelAndView.getModelMap().addAttribute("categoryIdKey",categoryKey);
        modelAndView.getModelMap().addAttribute("categoryId",categoryId);
        modelAndView.getModelMap().addAttribute("jobTitle",jobTitle);
        modelAndView.getModelMap().addAttribute("locationId",locationId);
        modelAndView.getModelMap().addAttribute("apply",(apply == null ? "" : apply));
        modelAndView.setViewName("jobDetail");
        return modelAndView;
    }
}
