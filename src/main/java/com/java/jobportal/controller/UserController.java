package com.java.jobportal.controller;

import com.java.jobportal.model.Education;
import com.java.jobportal.model.User;
import com.java.jobportal.model.WorkExperience;
import com.java.jobportal.service.EducationService;
import com.java.jobportal.service.UserService;
import com.java.jobportal.service.WorkExperiencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    UserService userService;
    
    @Autowired
    EducationService educationService;
    @Autowired
    WorkExperiencesService workExperiencesService;

    @PostMapping("/save")
    public String saveUser(User user) {
        userService.save(user);
        return "redirect:/admin/user/userList";
    }

    @PostMapping("/updateUser")
    public String updateUser(User user,@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        userService.save(user.getId(), user.isDisable());
        return "redirect:/admin/user/userList?page="+page;
    }

    @GetMapping("/userList")
    public String showUserList(Model model, @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        Page<User> userPage = userService.getUser(page, 5);
        List<User> userList = userPage.getContent();
        model.addAttribute("userList", userList);
        model.addAttribute("totalPages", userPage.getTotalPages() - 1);
        model.addAttribute("currentPage", userPage.getNumber());
        return "admin/user/userList";
    }

    @GetMapping("/detail/{id}")
    public String viewUserDetails(@PathVariable("id") Long id, Model model,
                                  @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        User user = userService.getById(id);
        
        if (user != null) {
            List<Education> educations = educationService.getEducationsByUserId(id);
            model.addAttribute("educations", educations);
            
            List<WorkExperience> experiences = workExperiencesService.getExperiencesByUserId(id);
            model.addAttribute("experiences", experiences);
        }
        model.addAttribute("user", user);
        model.addAttribute("currentPage", page);
        return "admin/user/userDetail";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id,
                           Model model,
                           @RequestParam(name = "page", required = false, defaultValue = "0") int page) throws Exception {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("currentPage", page);
        return "admin/user/editUser";
    }
    
}
