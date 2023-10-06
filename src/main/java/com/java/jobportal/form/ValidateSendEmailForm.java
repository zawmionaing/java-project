package com.java.jobportal.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ValidateSendEmailForm {
    @NotEmpty
    @Email
    private String email;
}
