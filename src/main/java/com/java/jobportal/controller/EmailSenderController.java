package com.java.jobportal.controller;

import com.java.jobportal.form.PasswordResetForm;
import com.java.jobportal.form.ValidateSendEmailForm;
import com.java.jobportal.model.PasswordReset;
import com.java.jobportal.model.User;
import com.java.jobportal.service.EmailSenderService;
import com.java.jobportal.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Controller
public class EmailSenderController {
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    UserService userService;
    
    @GetMapping(value = "/forgot_password")
    public String showForgotPasswordForm(Model model) throws MessagingException {
        model.addAttribute("validateSendEmailForm", new ValidateSendEmailForm());
        return "forgotPassword";
    }
    
    @PostMapping(value = "/requestForReset")
    public String resetPassword(@ModelAttribute("validateSendEmailForm") @Valid ValidateSendEmailForm validateSendEmailForm, BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "forgotPassword";
        } else {
            if (emailSenderService.checkAndSendEmail(validateSendEmailForm.getEmail())) {
                model.addAttribute("sendMessage", "An email has been sent to " + validateSendEmailForm.getEmail() + " to reset your password. \n\n Your token will be expired in 3 minutes. \n\n Click <a href='/home' class='fw-bolder'>HERE</a> to go back");
                return "emailSentSuccess";
                
            } else {
                model.addAttribute("message", "Couldn't find user with this email.");
                return "forgotPassword";
            }
        }
    }
    
    @GetMapping("/reset-password")
    public String showPasswordResetForm(@RequestParam("token") String token, Model model) {
        if (isValidResetToken(token)) {
            model.addAttribute("token", token);
            model.addAttribute("passwordResetForm", new PasswordResetForm());
            return "resetPassword";
        } else {
            model.addAttribute("message", "Your token is expired");
            model.addAttribute("validateSendEmailForm", new ValidateSendEmailForm());
            return "forgotPassword";
        }
    }
    
    @PostMapping("/requestPasswordChange")
    public String resetPassword(@ModelAttribute("passwordResetForm")
                                @Valid PasswordResetForm passwordResetForm, BindingResult bindingResult,
                                @RequestParam("token") String token,
                                RedirectAttributes redirectAttributes,
                                Model model) throws Exception {
        if (isValidResetToken(token)) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("token", token);
                return "resetPassword";
            } else {
                PasswordReset passwordReset = emailSenderService.getByToken(token);
                if (isValidPassword(passwordResetForm.getNewPassword(), passwordResetForm.getConfirmPassword())) {
                    Long userId = passwordReset.getUser().getId();
                    User user = userService.getById(userId);
                    user.setPassword(passwordResetForm.getNewPassword());
                    userService.saveOrUpdate(user);
                    emailSenderService.deleteById(passwordReset.getId());
                    redirectAttributes.addFlashAttribute("passwordReset", true);
                    return "redirect:/login";
                } else {
                    model.addAttribute("error", "Passwords do not match! Try again.");
                    return "resetPassword";
                }
            }
        } else {
            model.addAttribute("message", "Your token is expired");
            model.addAttribute("validateSendEmailForm", new ValidateSendEmailForm());
            return "forgotPassword";
        }
    }
    
    private boolean isValidResetToken(String token) {
        PasswordReset passwordReset = emailSenderService.getByToken(token);
        if (passwordReset == null) {
            return false;
        } else {
            LocalDateTime currentDateTime = LocalDateTime.now();
            long minutesDifference = ChronoUnit.MINUTES.between(passwordReset.getCreatedAt(), currentDateTime);
            
            return minutesDifference <= 3;
        }
    }
    
    private boolean isValidPassword(String newPassword, String confirmPassword) {
        return Objects.equals(newPassword, confirmPassword);
    }
}
