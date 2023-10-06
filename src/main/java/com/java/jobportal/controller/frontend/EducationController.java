package com.java.jobportal.controller.frontend;

import com.java.jobportal.form.EducationForm;
import com.java.jobportal.model.Education;
import com.java.jobportal.model.User;
import com.java.jobportal.service.UserService;
import com.java.jobportal.serviceImpl.EducationServiceImpl;
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
public class EducationController {
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

    @Autowired
    EducationServiceImpl educationServiceImpl;
    @GetMapping("/user/profile/createEdu")
    public String directCreateEduPage(Model model) throws Exception {
        Education education=new Education();
        User user = userService.getById(getAuthenticatedUser());
        education.setUser(user);
        model.addAttribute("education", education);
        return "user/createEducation";

    }


    @PostMapping("/user/saveEducation")
    public String saveEducation(@ModelAttribute("education") @Valid EducationForm educationForm, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "user/createEducation";
        }
        User user= userService.getById(getAuthenticatedUser());
        educationForm.setUser(user);
        Education education=new Education(educationForm);
        educationServiceImpl.createEducation(education);
        return "redirect:/user/profile" ;
    }


    @GetMapping("/user/editEducation/{id}")
    public String editEducation(@PathVariable Long id, Model model) {
        Education education = educationServiceImpl.getEducationById(id);
        model.addAttribute("education", education);
        return "user/updateEducation";
    }


    @PostMapping("/user/updateEducation/{id}")
    public String updateEducation(@ModelAttribute("education") @Valid EducationForm educationForm, BindingResult bindingResult,@PathVariable Long id ) {
        if (bindingResult.hasErrors()) {
            return "user/updateEducation";
        }
        Education existingEducation = educationServiceImpl.getEducationById(id);
        if (existingEducation != null) {
            
            existingEducation.setName(educationForm.getName());
            educationServiceImpl.updateEducation(existingEducation);
        }

        return "redirect:/user/profile" ;
    }


    @GetMapping("/user/deleteEducation/{id}")
    public String deleteEdu(@PathVariable Long id) {

        educationServiceImpl.deleteEducation(id);

        return "redirect:/user/profile" ;
    }



}
