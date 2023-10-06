package com.java.jobportal.service;

import com.java.jobportal.form.ContactForm;
import com.java.jobportal.model.PasswordReset;
import com.java.jobportal.model.User;
import org.springframework.stereotype.Service;

@Service
public interface EmailSenderService {
    
    default boolean checkAndSendEmail(String toEmail) throws Exception {
        return checkAndSendEmail(toEmail);
    }

    void sendContactMail(ContactForm contactForm);
    
    void deleteById(Long id);
    
    PasswordReset getByToken(String token);

}
