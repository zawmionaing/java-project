package com.java.jobportal.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterForm {

    @Size(min = 4, max = 20, message = "Name must be between 4 and 20 words")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Enter valid email")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
