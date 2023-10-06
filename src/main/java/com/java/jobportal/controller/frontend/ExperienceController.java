package com.java.jobportal.controller.frontend;

import com.java.jobportal.form.WorkExperienceForm;
import com.java.jobportal.model.User;
import com.java.jobportal.model.UserProfile;
import com.java.jobportal.model.WorkExperience;
import com.java.jobportal.service.UserService;
import com.java.jobportal.service.WorkExperiencesService;
import com.java.jobportal.serviceImpl.WorkExperiencesServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExperienceController {
    
    @Autowired
    WorkExperiencesServiceImpl workExperiencesServiceImp;
    
    @Autowired
    WorkExperiencesService workExperiencesService;
    
    @Autowired
    UserService userService;
    
    public Long getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String currentLoginName = authentication.getName();
        return userService.getUserByName(currentLoginName).getId();
    }
    
    @GetMapping("/user/profile/createExp")
    public String directCreateExpPage(Model model) throws Exception {
        WorkExperience workExperience = new WorkExperience();
        User user = userService.getById(getAuthenticatedUser());
        workExperience.setUser(user);
        model.addAttribute("workExperience", workExperience);
        return "user/createWorkExperience";
    }
    
    @PostMapping("/user/saveExp")
    public String saveExperience(@ModelAttribute("workExperience") @Valid WorkExperienceForm workExperienceForm, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "user/createWorkExperience";
        }
        User user = userService.getById(getAuthenticatedUser());
        workExperienceForm.setUser(user);
        WorkExperience workExperience = new WorkExperience(workExperienceForm);
        workExperiencesServiceImp.createExperience(workExperience);
        return "redirect:/user/profile";
    }
    
    @GetMapping("/user/exp/update/{id}")
    public String directEditExpPage(Model model, @PathVariable Long id) throws Exception {
        WorkExperience workExperience = workExperiencesServiceImp.getExperienceById(id);
        model.addAttribute("workExperience", workExperience);
        return "user/editWorkExperience";
    }
    
    @PostMapping("/user/editExp/{expId}")
    public String updateExperience(@ModelAttribute("workExperience") @Valid WorkExperienceForm workExperienceForm, BindingResult bindingResult, @PathVariable Long expId, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "user/editWorkExperience";
        }
        WorkExperience existingExperience = workExperiencesServiceImp.getExperienceById(expId);
        existingExperience.setJobTitle(workExperienceForm.getJobTitle());
        existingExperience.setCompanyName(workExperienceForm.getCompanyName());
        existingExperience.setStartDate(workExperienceForm.getStartDate());
        existingExperience.setEndDate(workExperienceForm.getEndDate());
        existingExperience.setDescription(workExperienceForm.getDescription());
        
        workExperiencesServiceImp.updateExperience(existingExperience);
        return "redirect:/user/profile";
    }
    
    @GetMapping("/user/exp/delete/{id}")
    public String deleteWorkExperience(@PathVariable Long id) {
        workExperiencesServiceImp.deleteWorkExperience(id);
        
        return "redirect:/user/profile";
    }
    
}
