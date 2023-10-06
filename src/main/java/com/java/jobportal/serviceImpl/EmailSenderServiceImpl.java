package com.java.jobportal.serviceImpl;

import com.java.jobportal.form.ContactForm;
import com.java.jobportal.model.PasswordReset;
import com.java.jobportal.model.User;
import com.java.jobportal.repository.PasswordResetRepository;
import com.java.jobportal.repository.UserRepository;
import com.java.jobportal.service.EmailSenderService;
import com.java.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    @Autowired
    PasswordResetRepository passwordResetRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    UserService userService;
    
    @Override
    public boolean checkAndSendEmail(String toEmail) throws Exception {
        User user = userRepository.findByEmail(toEmail);
        if (user == null) {
            return false;
        }
        else {
            
            PasswordReset token = new PasswordReset();
            token.setUser(user);
            token.setEmail(toEmail);
            token.setToken(UUID.randomUUID().toString());
            
            passwordResetRepository.save(token);
            
            SimpleMailMessage message = getSimpleMailMessage(toEmail, token, user);
            javaMailSender.send(message);
        }
        return true;
    }
    
    @Override
    public PasswordReset getByToken(String token) {
        return passwordResetRepository.findByToken(token);
    }
    
    @Override
    public void deleteById(Long id) {
        passwordResetRepository.deleteById(id);
    }
    
    private static SimpleMailMessage getSimpleMailMessage(String toEmail, PasswordReset token, User user) {
        String resetUrl="http://localhost:8081/reset-password?token=" + token.getToken();
        String adminMain="zawmyonaing.dev@gmail.com";
        String body= "Dear " + user.getName() + ", \n\nWe received a request to reset your password for your account. " +
            "\n\nTo ensure the security of your account, please follow the instructions below: " +
            "\n\nClick on the following link to reset your password: \n" + resetUrl+"\n\nEnsure that you create a strong password by using a combination of \nuppercase and lowercase letters, numbers, and special characters. " +
            "\nDo not share your password or this email with anyone for security reasons. \n\n" +
            "If you have any questions or need further assistance, \nplease feel free to contact our support team at "+adminMain+". \n\n " +
            "Thank you for using our website platform! \n\n\n" +
            "Sincerely,\n\n\n" +
            "Admin Team \n" +
            "Job Portal\n"+ adminMain;
        String subject = "Password Reset Request";
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(adminMain);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        return message;
    }

    @Override
    public void sendContactMail(ContactForm contactForm){
        String userMessage = "From User : "+ contactForm.getName() +"\nUser Email : "+ contactForm.getEmail() +"\n\n\nMessage : \n"+ contactForm.getMessage();
        SimpleMailMessage message = new SimpleMailMessage();
        User admin = userService.getUserByName("admin");
        message.setFrom(contactForm.getEmail());
        message.setTo(admin.getEmail());
        message.setSubject("Job Port Contact Mail : "+contactForm.getSubject());
        message.setText(userMessage);
        javaMailSender.send(message);
    }
    
    
}
