package com.java.jobportal.controller;

import com.java.jobportal.model.Job;
import com.java.jobportal.model.Status;
import com.java.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class JobController {
    @Autowired
    JobService jobService;

    @RequestMapping(value = "admin/job/jobList", method = RequestMethod.GET)
    public ModelAndView showJobList(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {

        ModelAndView modelAndView = new ModelAndView();
        Page<Job> jobsPage = jobService.getAllJob(page, 5);
        List<Job> jobs = jobsPage.getContent();

        modelAndView.addObject("jobs", jobs);
        modelAndView.getModelMap().addAttribute("totalPages", jobsPage.getTotalPages() - 1);
        modelAndView.getModelMap().addAttribute("currentPage", jobsPage.getNumber());
        modelAndView.setViewName("admin/job/jobList");
        return modelAndView;
    }

    @RequestMapping(value = "admin/job/jobDetail/{id}", method = RequestMethod.GET)
    public ModelAndView showJobDetails(Job job, @PathVariable Long id, @RequestParam("page") int page) {
        Job jobs = jobService.getJobById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("jobs", jobs);
        modelAndView.addObject("currentPage", page);
        modelAndView.setViewName("admin/job/jobDetail");
        return modelAndView;
    }

    @RequestMapping(value = "admin/updateJob", method = RequestMethod.POST)
    public String updateJobStatus(@RequestParam("id") Long id, @RequestParam("status") Status status) {
        jobService.updateJob(id, status);
        return "redirect:/admin/job/jobList";
    }

    @GetMapping("admin/job/toggleJobStatus/{id}")
    public String toggleJobStatus(@PathVariable("id") Long id, @RequestParam("page") int page, Model model, RedirectAttributes redirectAttributes) throws Exception {
        jobService.toggleJobStatus(id);
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/admin/job/jobList?page=" + page;
    }
}
