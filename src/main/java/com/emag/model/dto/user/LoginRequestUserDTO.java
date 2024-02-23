package com.emag.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequestUserDTO {
    @NotBlank(message = "Email is mandatory!")
    @Email(message = "Invalid email format.")
    private String email;
    @NotBlank(message = "Password is mandatory!")
    private String password;
}
