package com.java.jobportal.controller;

import com.java.jobportal.form.ContactForm;
import com.java.jobportal.service.EmailSenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
public class ContactController {

    @Autowired
    EmailSenderService emailSenderService;

    @RequestMapping(value = "/contact/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<?> getSearchResultViaAjax(@RequestBody @Valid ContactForm contactForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            contactForm.setError(true);
            return ResponseEntity.ok(contactForm);
        }
        emailSenderService.sendContactMail(contactForm);
        return ResponseEntity.ok(contactForm);
    }
}
