package com.java.jobportal.form;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileForm { ;
    
    private String cvForm;
    
    private LocalDate dateOfBirth;
    
    @Size( max = 25, message = "NRC must be less than 25 characters")
    private String nrc;
    
    @Size( max = 100, message = "Address must be less than 100 characters")
    private String address;
    
    private String photo;
    
    @Pattern(regexp = "^[0-9]*$", message = "Phone number must contain only digits")
    private String phone;
    
}
