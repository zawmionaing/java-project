package com.java.jobportal.controller;

import com.java.jobportal.form.CompanyRegisterForm;
import com.java.jobportal.form.UserRegisterForm;
import com.java.jobportal.model.Company;
import com.java.jobportal.model.User;
import com.java.jobportal.service.CompanyService;
import com.java.jobportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @RequestMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

    @GetMapping("/registerForm")
    public ModelAndView showRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        UserRegisterForm userRegisterForm = new UserRegisterForm();
        modelAndView.addObject("userRegisterForm", userRegisterForm);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute("userRegisterForm") @Valid UserRegisterForm userRegisterForm, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else {
            boolean isNameUnique = userService.isNameUnique(userRegisterForm.getName());
            boolean isEmailUnique = userService.isEmailUnique(userRegisterForm.getEmail());

            if (!isNameUnique) {
                bindingResult.rejectValue("name", "error.userRegisterForm", "Name is already in use");
            }
            if (!isEmailUnique) {
                bindingResult.rejectValue("email", "error.userRegisterForm", "Email is already in use");
            }
            if (bindingResult.hasErrors()) {
                modelAndView.setViewName("register");
            } else {
                userService.saveOrUpdate(new User(userRegisterForm));
                modelAndView.setViewName("redirect:/login");
                redirectAttributes.addFlashAttribute("registerSuccess", true);
            }
        }
        return modelAndView;
    }
    
    @GetMapping("/companyRegisterForm")
    public ModelAndView showCompanyRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView("company-register");
        modelAndView.addObject("companyRegisterForm", new CompanyRegisterForm());
        return modelAndView;
    }

    @PostMapping("/registerCompany")
    public ModelAndView registerCompany(@ModelAttribute("companyRegisterForm") @Valid CompanyRegisterForm companyRegisterForm, BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("company-register"); // Return the same view if there are validation errors
        } else {
            boolean isNameUnique = userService.isNameUnique(companyRegisterForm.getName());
            boolean isEmailUnique = userService.isEmailUnique(companyRegisterForm.getEmail());
            boolean isCompanyNameUnique = companyService.isCompanyNameUnique(companyRegisterForm.getCompanyName());

            if (!isNameUnique) {
                bindingResult.rejectValue("name", "error.companyRegisterForm", "Name is already in use");
            }
            if (!isEmailUnique) {
                bindingResult.rejectValue("email", "error.companyRegisterForm", "Email is already in use");
            }
            if (!isCompanyNameUnique) {
                bindingResult.rejectValue("companyName", "error.companyRegisterForm", "Company name is already taken");
            }

            if (bindingResult.hasErrors()) {
                modelAndView.setViewName("company-register");
            } else {
                User user = new User(companyRegisterForm);
                userService.saveOrUpdate(user);
                companyService.saveOrUpdate(new Company(companyRegisterForm, user));
                modelAndView.setViewName("redirect:/login");
                redirectAttributes.addFlashAttribute("registerSuccess", true);
            }
        }
        return modelAndView;
    }
}
