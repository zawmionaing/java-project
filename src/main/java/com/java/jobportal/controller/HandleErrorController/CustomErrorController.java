package com.java.jobportal.controller.HandleErrorController;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    
    @RequestMapping("/404")
    public String handle404() {
        return "404";
    }
    
}