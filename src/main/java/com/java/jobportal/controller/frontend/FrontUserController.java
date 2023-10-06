package com.java.jobportal.controller.frontend;

import com.java.jobportal.form.ChangePasswordForm;
import com.java.jobportal.form.UserProfileForm;
import com.java.jobportal.model.Education;
import com.java.jobportal.model.User;
import com.java.jobportal.model.UserProfile;
import com.java.jobportal.model.WorkExperience;
import com.java.jobportal.service.EducationService;
import com.java.jobportal.service.UserProfileService;
import com.java.jobportal.service.UserService;
import com.java.jobportal.service.WorkExperiencesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class FrontUserController {
    @Autowired
    UserService userService;
    @Autowired
    EducationService educationService;
    @Autowired
    WorkExperiencesService workExperiencesService;
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public Long getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        String currentLoginName = authentication.getName();
        return userService.getUserByName(currentLoginName).getId();
    }
    
    @GetMapping("/uploadProfile")
    public String uploadProfile(Model model) throws Exception {
        if (getAuthenticatedUser()==null){
            return "redirect:/login";
        }
        User user=userService.getById(getAuthenticatedUser());
        model.addAttribute("user",user);
        model.addAttribute("userProfileForm", new UserProfileForm());
        return "user/userProfile";
    }
    
    @PostMapping("/saveProfile")
    public String saveProfile(@ModelAttribute("userProfileForm") @Valid UserProfileForm userProfileForm, BindingResult bindingResult,
                              @RequestParam("new_photo") MultipartFile new_photo,
                              @RequestParam("new_cvForm") MultipartFile new_cvForm,
                              Model model) throws Exception {
        
        if (getAuthenticatedUser()==null){
            return "redirect:/login";
        }
        if (new_photo != null && !new_photo.isEmpty() && StringUtils.hasText(new_photo.getOriginalFilename())) {
            if(!isImageFile(new_photo)){
                bindingResult.rejectValue("photo", "file.invalidType", "File must be an image (jpg, jpeg, png, gif)");
            }
            if(isValidSize(new_photo, 2 * 1024 * 1024)){
                bindingResult.rejectValue("photo", "file.sizeExceeded", "File size must not exceed 2MB");
            }
        }
        if (new_cvForm != null && !new_cvForm.isEmpty() && StringUtils.hasText(new_cvForm.getOriginalFilename())) {
            if(isValidSize(new_cvForm, 3 * 1024 * 1024)){
                bindingResult.rejectValue("cvForm", "file.sizeExceeded", "File size must not exceed 3MB");
            }
            if(!isPDFFile(new_cvForm)){
                bindingResult.rejectValue("cvForm", "file.invalidType", "CV Form must be a PDF");
            }
        }
        
        User user = userService.getById(getAuthenticatedUser());
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("user",user);
            return "user/userProfile";
        }else {
            UserProfile userProfile = new UserProfile(userProfileForm);
            userProfile.setUser(user);
            userProfileService.saveOrUpdate(userProfile, new_photo, new_cvForm);
            return "redirect:/user/profile";
        }
    }
    
    @GetMapping("/profile")
    public String showUserProfile(Model model) throws Exception {
        if (getAuthenticatedUser()==null){
            return "redirect:/login";
        }
        User userDetail = userService.getById(getAuthenticatedUser());
        
        if (userDetail != null) {
            List<Education> educations = educationService.getEducationsByUserId(getAuthenticatedUser());
            model.addAttribute("educations", educations);
            
            List<WorkExperience> experiences = workExperiencesService.getExperiencesByUserId(getAuthenticatedUser());
            model.addAttribute("experiences", experiences);
        }
            model.addAttribute("userDetail", userDetail);
        return "user/profile";
    }
    
    @GetMapping("/profile/edit")
    public  String showUserProfileEditForm(Model model) throws Exception {
        if (getAuthenticatedUser()==null){
            return "redirect:/login";
        }
        User user = userService.getById(getAuthenticatedUser());
        UserProfile userProfile= userProfileService.getById(user.getUserProfile().getId());
        model.addAttribute("user", user);
        model.addAttribute("userProfileForm", userProfile);
        return "user/userProfile";
        
    }
    
    private boolean isImageFile(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            return contentType != null && contentType.startsWith("image/");
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isPDFFile(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            return contentType != null && contentType.endsWith("/pdf");
        } catch (Exception e) {
            return false;
        }
    }
    private  boolean isValidSize(MultipartFile file, long MAX_IMAGE_SIZE) {
        try {
            return file.getSize() > MAX_IMAGE_SIZE;
        } catch (Exception e) {
            return true;
        }
    }



}



