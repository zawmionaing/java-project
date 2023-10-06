package com.java.jobportal.controller;

import com.java.jobportal.model.Company;

import com.java.jobportal.service.CompanyService;
import com.java.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;
    
    @GetMapping("/companyList")
    public String showCompanyList(Model model,
              @RequestParam(name ="page", required = false, defaultValue = "0") int page
    ) {
        Page<Company> companyPage =companyService.getCompany(page,5);
        List<Company>  companyList=companyPage.getContent();
        model.addAttribute("companyList",companyList);
        model.addAttribute("totalPages", companyPage.getTotalPages() - 1);
        model.addAttribute("currentPage", companyPage.getNumber());
        return "admin/company/companyList";
    }

    @GetMapping("/detail/{id}")
    public String viewCompany(@PathVariable("id") Long id, @RequestParam(name = "page", required = false, defaultValue = "0") int currentPage, Model model) throws Exception {
        Company company = companyService.getCompanyById(id);
        model.addAttribute("company", company);
        model.addAttribute("currentPage", currentPage); // Pass the current page number to the view
        return "admin/company/companyDetail";
    }

    @GetMapping("/toggleStatusAndRole/{id}")
    public String toggleStatusAndRole(
            @PathVariable("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) throws Exception {
        companyService.toggleStatusAndRole(id);
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/admin/company/companyList?page=" + page;
    }

    @PostMapping("/save")
    public String saveCompany(
            Company company,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        companyService.saveOrUpdate(company);
        return "redirect:/admin/company/companyList?page=" + page;
    }

}
