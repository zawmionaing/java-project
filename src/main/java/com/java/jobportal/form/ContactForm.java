package com.java.jobportal.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

@Data
@NoArgsConstructor
public class ContactForm {

    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private  String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Enter valid email")
    private String email;

    @NotEmpty(message = "Subject is required")
    private String subject;

    @NotEmpty(message = "Message is required")
    private String message;

    private Boolean error=false;
}
