package com.java.jobportal.controller.companyRole;

import com.java.jobportal.form.JobForm;
import com.java.jobportal.model.AuthUser;
import com.java.jobportal.model.Job;
import com.java.jobportal.model.SubCategory;
import com.java.jobportal.model.Township;
import com.java.jobportal.service.JobService;
import com.java.jobportal.service.SubCategoryService;
import com.java.jobportal.service.TownshipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/company")
public class JobRoleController {

    @Autowired
    JobService jobService;

    @Autowired
    TownshipService townshipService;

    @Autowired
    SubCategoryService subCategoryService;

    @RequestMapping(value = "/jobList", method = RequestMethod.GET)
    public ModelAndView showJobList(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        Page<Job> jobsPage = jobService.getAllJob(authUser, page, 5);
        List<Job> jobs = jobsPage.getContent();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("jobs", jobs);
        modelAndView.getModelMap().addAttribute("totalPages", jobsPage.getTotalPages() - 1);
        modelAndView.getModelMap().addAttribute("currentPage", jobsPage.getNumber());
        modelAndView.setViewName("company/jobList");
        return modelAndView;
    }

    @GetMapping("/jobCreateForm")
    public String createJob(Model model,@RequestParam(name = "page", defaultValue = "0") int page) throws Exception {
        JobForm jobForm = new JobForm();
        model.addAttribute("jobForm", jobForm);
        model.addAttribute("currentPage",page);
        model.addAttribute("subcategories", subCategoryService.getAllSubCategories(0, Integer.MAX_VALUE).getContent());
        model.addAttribute("townships", townshipService.getTownships(0, Integer.MAX_VALUE).getContent());
        return "company/jobCreate";
    }

    @PostMapping("/jobCreate")
    public String createJob(@ModelAttribute("jobForm") @Valid JobForm jobForm,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal AuthUser user,
                            @RequestParam(name = "currentPage", required = false, defaultValue = "0") int page,
                            Model model, RedirectAttributes redirectAttributes) throws IOException {
        MultipartFile jobPhotoImg = jobForm.getJobPhotoImg();

        if (jobPhotoImg.getOriginalFilename() != "") {
            if (!jobForm.isImage()) {
                bindingResult.rejectValue("jobPhotoImg", "error.jobForm", "Uploaded file is not an image");
            } else if (!jobForm.isSizeValid()) {
                bindingResult.rejectValue("jobPhotoImg", "error.jobForm", "Image size cannot exceed 2MB");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentPage",page);
            model.addAttribute("subcategories", subCategoryService.getAllSubCategories(0, Integer.MAX_VALUE).getContent());
            model.addAttribute("townships", townshipService.getTownships(0, Integer.MAX_VALUE).getContent());
            return "company/jobEdit";
        }

        jobService.createJob(jobForm, jobPhotoImg, user);
        model.addAttribute("currentPage",page);
        redirectAttributes.addFlashAttribute("jobCreated", true);
        return "redirect:/company/jobList?page=" + page;
    }

    @RequestMapping(value = "/jobDetail/{id}", method = RequestMethod.GET)
    public ModelAndView showJobDetails(Job job, @PathVariable Long id,@RequestParam(name = "page", defaultValue = "0") int page) {
        Job jobs = jobService.getJobById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("jobs", jobs);
        modelAndView.addObject("currentPage",page);
        modelAndView.setViewName("company/jobDetail");
        return modelAndView;
    }

    @RequestMapping(value = "/jobEdit/{id}", method = RequestMethod.GET)
    public ModelAndView editJob(JobForm jobForm, @PathVariable Long id,@RequestParam(name = "page", defaultValue = "0") int page) {
        Job job = jobService.getJobById(id);
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories(0, Integer.MAX_VALUE).getContent();
        List<Township> townships = townshipService.getTownships(0, Integer.MAX_VALUE).getContent();
        jobForm = new JobForm(job);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("jobForm", jobForm);
        modelAndView.addObject("subcategories", subCategories);
        modelAndView.addObject("townships", townships);
        modelAndView.addObject("currentPage",page);
        modelAndView.setViewName("company/jobEdit");
        return modelAndView;
    }
}
