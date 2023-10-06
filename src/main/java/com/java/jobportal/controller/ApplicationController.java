package com.java.jobportal.controller;

import com.java.jobportal.model.Application;
import com.java.jobportal.model.AuthUser;
import com.java.jobportal.model.User;
import com.java.jobportal.model.Job;
import com.java.jobportal.service.ApplicationService;
import com.java.jobportal.service.JobService;
import com.java.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    UserService userService;

    @Autowired
    JobService jobService;

    @Autowired
    ApplicationService applicationService;

    @RequestMapping(value = "/user/saveApplication/{jobId}",method = RequestMethod.GET)
    public String saveApplication(
            @PathVariable Long jobId,
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "jobtype",required = false,defaultValue = "") String jobType,
            @RequestParam(name = "categoryKey",required = false) Long categoryKey,
            @RequestParam(name = "categoryId",required = false) Long categoryId,
            @RequestParam(name = "jobTitle",required = false,defaultValue = "") String jobTitle,
            @RequestParam(name = "locationId",required = false)Long locationId
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User authUser = userService.getUserByName(currentPrincipalName);
        Job job = jobService.getJobById(jobId);
        Application application = new Application();
        application.setUser(authUser);
        application.setJob(job);
        applicationService.saveOrUpdateApplication(application);
        return "redirect:/jobs?page="+page+"&jobtype="+jobType+"&categoryKey="+(categoryKey == null ? "" : categoryKey)+"&jobTitle="+jobTitle+"&categoryId="+(categoryId == null ? "" : categoryId)+"&locationId="+(locationId == null ? "" : locationId);
    }

    @RequestMapping(value= "/user/appliedJob",method = RequestMethod.GET)
    public ModelAndView directAplliedJobsPage(

            @RequestParam(name ="page", required = false, defaultValue = "0") int page
    ){
        ModelAndView modelAndView;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User authUser = userService.getUserByName(currentPrincipalName);
        Page<Application> applicationsPage = applicationService.getByUserId(authUser,page,5);
        List<Application> applications = applicationsPage.getContent();
        if(page>0 && applications.isEmpty()){
            modelAndView = new ModelAndView("redirect:/user/appliedJob?page="+(applicationsPage.getTotalPages() -1));
        }else{
            modelAndView = new ModelAndView("/user/appliedJobList");
        }
        modelAndView.addObject("applications",applications);
        modelAndView.getModelMap().addAttribute("totalPages", applicationsPage.getTotalPages() - 1);
        modelAndView.getModelMap().addAttribute("currentPage", applicationsPage.getNumber());
        return modelAndView;
    }

    @RequestMapping(value = "/user/deleteAppliedJob/{id}",method = RequestMethod.GET)
    public String deleteApplication(@PathVariable Long id,@RequestParam(name ="page", required = false, defaultValue = "0") int page){
        applicationService.deleteById(id);
        return "redirect:/user/appliedJob?page=" + page;
    }
}
