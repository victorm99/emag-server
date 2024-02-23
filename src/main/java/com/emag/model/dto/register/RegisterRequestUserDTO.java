package com.emag.model.dto.register;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegisterRequestUserDTO {
    @NotBlank(message = "Email is mandatory!")
    @Email(message = "Invalid email format.")
    private String email;
    @NotBlank(message = "Name is mandatory!")
    private String fullName;
    @NotBlank(message = "Password is mandatory!")
    private String password;
    @NotBlank(message = "Password confirmation is mandatory!")
    private String confirmPassword;
    private boolean isAdmin;
}
