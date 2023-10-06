package com.java.jobportal.controller;

import com.java.jobportal.model.Township;
import com.java.jobportal.service.TownshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TownshipController {

    @Autowired
    TownshipService townshipService;

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public ModelAndView directLocationPage(Township township,
                                           @RequestParam(name ="page", required = false, defaultValue = "0") int page


    ){
        Page<Township> townshipsPage = townshipService.getTownships(page,5);
        List<Township> townships=townshipsPage.getContent();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("townships",townships);
        modelAndView.getModelMap().addAttribute("totalPages", townshipsPage.getTotalPages() - 1);
        modelAndView.getModelMap().addAttribute("currentPage", townshipsPage.getNumber());
        modelAndView.setViewName("/admin/location/location");
        return modelAndView;
    }
}
