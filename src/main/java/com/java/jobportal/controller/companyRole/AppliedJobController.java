package com.java.jobportal.controller.companyRole;

import com.java.jobportal.model.*;
import com.java.jobportal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/company")
public class AppliedJobController {

    @Autowired
    ApplicationService applicationService;

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @Autowired
    EducationService educationService;

    @Autowired
    WorkExperiencesService workExperiencesService;

    @Autowired
    JobService jobService;

    @RequestMapping(value= "/appliedJob",method = RequestMethod.GET)
    public ModelAndView directAplliedJobsPage(@AuthenticationPrincipal AuthUser authUser,
         @RequestParam(name ="page", required = false, defaultValue = "0") int page
    ) throws Exception {

        ModelAndView modelAndView = new ModelAndView();
        Company company=companyService.getCompanyById(authUser);

        List<Job> jobs = jobService.getJobsByCompanyId(company);
        Page<Application> applicationsPage = applicationService.getByJobId(jobs,page,5);
        List<Application> applications=applicationsPage.getContent();
        modelAndView.addObject("applications",applications);
        modelAndView.getModelMap().addAttribute("totalPages", applicationsPage.getTotalPages() - 1);
        modelAndView.getModelMap().addAttribute("currentPage", applicationsPage.getNumber());
        modelAndView.setViewName("/company/appliedJobList");
        return modelAndView;
    }

    @RequestMapping(value = "/userProfile/{id}",method = RequestMethod.GET)
    public ModelAndView showUserProfile(Model model, @PathVariable Long id, @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Application application = applicationService.getById(id);
        Long userId = application.getUser().getId();
        User userDetail = userService.getById(userId);

        if (userDetail != null) {
            List<Education> educations = educationService.getEducationsByUserId(userId);
            modelAndView.getModelMap().addAttribute("educations", educations);

            List<WorkExperience> experiences = workExperiencesService.getExperiencesByUserId(userId);
            modelAndView.getModelMap().addAttribute("experiences", experiences);
        }
        modelAndView.addObject("applicationObj",application);
        modelAndView.addObject("userDetail", userDetail);
        modelAndView.getModelMap().addAttribute("currentPage",page);
        modelAndView.setViewName("/company/userProfile");
        return modelAndView;
    }

    @RequestMapping(value = "/employed/{id}",method = RequestMethod.GET)
    public String employedUser(@PathVariable Long id,
                               @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                               @RequestParam(name = "apply", required = false) boolean apply
    ){
        Application application = applicationService.getById(id);
        if(apply == true){
            application.setStatus(Status.APPROVED);
        }else{
            application.setStatus(Status.REJECTED);
        }
        applicationService.saveOrUpdateApplication(application);
        return "redirect:/company/appliedJob?page="+page;
    }
}
