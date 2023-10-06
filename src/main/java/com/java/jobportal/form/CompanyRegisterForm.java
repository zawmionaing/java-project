package com.java.jobportal.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyRegisterForm {

    @Size(min = 4, max = 20, message = "Name must be between 4 and 20 words")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Enter valid email")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotEmpty(message = "Company name is required")
    @Size(max = 255, message = "Company name must not exceed 255 characters")
    private String companyName;

    @Pattern(regexp = "^[0-9]*$", message = "Phone number must contain only digits")
    @NotEmpty(message = "Phone is required")
    private String phone;

    @NotEmpty(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
}
