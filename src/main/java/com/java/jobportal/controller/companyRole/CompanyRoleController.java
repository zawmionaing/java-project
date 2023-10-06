package com.java.jobportal.controller.companyRole;

import com.java.jobportal.form.CompanyEditForm;
import com.java.jobportal.model.*;
import com.java.jobportal.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/company")
public class CompanyRoleController {
    @Autowired
    CompanyService companyService;

    @RequestMapping(value = "/companyDetail", method = RequestMethod.GET)
    public ModelAndView viewCompanyDetails(Model model, @AuthenticationPrincipal AuthUser authUser) throws Exception {
        Company company = companyService.getCompanyById(authUser);
        return new ModelAndView("company/companyDetail", "company", company);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView editCompany(Model model, @AuthenticationPrincipal AuthUser authUser) throws Exception {
        CompanyEditForm companyEditForm = new CompanyEditForm(companyService.getCompanyById(authUser));
        return new ModelAndView("company/companyEdit", "companyEditForm",companyEditForm);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView saveCompany(@Valid Company company, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("company/companyEdit");
        } else {
            companyService.saveOrUpdate(company);
            modelAndView.setViewName("redirect:/company/companyDetail");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/updateCompany", method = RequestMethod.POST)
    public ModelAndView updateCompany(@ModelAttribute("companyEditForm") @Valid CompanyEditForm companyEditForm, BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        MultipartFile logoImg = companyEditForm.getLogoImg();
        MultipartFile coverPhotoImg = companyEditForm.getCoverPhotoImg();
        System.out.println(companyEditForm.toString());
        System.out.println(companyEditForm.getId());
        if (logoImg.getOriginalFilename() != "") {
            if (!companyEditForm.isImage(logoImg)) {
                bindingResult.rejectValue("logoImg", "error.jobForm", "Uploaded logo file is not an image");
            } else if (!companyEditForm.isSizeValid(logoImg, 2 * 1024 * 1024)) {
                bindingResult.rejectValue("logoImg", "error.jobForm", "Logo image size cannot exceed 2MB");
            }
        }
        if (coverPhotoImg.getOriginalFilename() != "") {
            if (!companyEditForm.isImage(coverPhotoImg)) {
                bindingResult.rejectValue("coverPhotoImg", "error.jobForm", "Uploaded cover photo file is not an image");
            } else if (!companyEditForm.isSizeValid(coverPhotoImg, 2 * 1024 * 1024)) {
                bindingResult.rejectValue("coverPhotoImg", "error.jobForm", "Cover photo size cannot exceed 2MB");
            }
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("company/companyEdit");
        } else {
            companyService.saveOrUpdate(companyEditForm, logoImg, coverPhotoImg);
            redirectAttributes.addFlashAttribute("success",true);
            modelAndView.setViewName("redirect:/company/companyDetail");
        }
        return modelAndView;
    }
}
