package com.java.jobportal.controller;

import com.java.jobportal.form.ChangePasswordForm;
import com.java.jobportal.model.Category;
import com.java.jobportal.model.Location;
import com.java.jobportal.model.User;
import com.java.jobportal.repository.LocationRepository;
import com.java.jobportal.service.CategoryService;
import com.java.jobportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {
@Autowired
    CategoryService categoryService;
@Autowired
    LocationRepository locationRepository;

@Autowired
    UserService userService;

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
    @GetMapping("/contact")
    public String contactUs() {
        return "/contact";
    }

    @GetMapping("/wrong")
    public String error404() {
        return "/404";
    }

    @GetMapping("/about")
    public String aboutUs() {
        return "/about";
    }

    @RequestMapping(value = {"/","/home"},method = RequestMethod.GET)
    public String showCategories(Model model) {
        List<Category> category = categoryService.getAllCategories();
        List<Location> locations = locationRepository.findAll();
        model.addAttribute("locations", locations);
        model.addAttribute("category", category);
        return "/home";
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(ChangePasswordForm changePasswordForm, Model model) throws Exception {
        model.addAttribute("changePasswordForm", changePasswordForm);
        return "changePassword";
    }

    @PostMapping("/passwordChange")
    public String changePassword(@ModelAttribute("changePasswordForm") @Valid ChangePasswordForm changePasswordForm,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) throws Exception {
        if (bindingResult.hasErrors()) {

            return "changePassword";
        }
        if (getAuthenticatedUser()==null){
            return "redirect:/login";
        }
        User userDetail = userService.getById(getAuthenticatedUser());
        if (!passwordEncoder.matches(changePasswordForm.getCurrentPassword(), userDetail.getPassword())) {
            model.addAttribute("error", "Incorrect current password. Please try again.");
            return "changePassword";
        }else {
            if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())) {
                model.addAttribute("error", "Password mismatch");
                return "changePassword";
            }else {
                userService.updatePassword(userDetail, changePasswordForm.getNewPassword());
                redirectAttributes.addFlashAttribute("passwordChange", true);
                return "redirect:/login";
            }
        }

    }
}
