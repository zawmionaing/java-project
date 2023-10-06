package com.java.jobportal.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordForm {
    
    @NotEmpty
    @NotBlank
    @Size(min = 8)
    private String currentPassword;
    
    @NotEmpty
    @NotBlank
    @Size(min = 8)
    private String newPassword;
    
    @NotEmpty
    @NotBlank
    @Size(min = 8)
    private String confirmPassword;
}
