package com.java.jobportal.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetForm {
    @NotBlank
    @Size(min = 8)
    private String newPassword;
    
    @NotBlank
    private String confirmPassword;
}
