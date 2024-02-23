package com.emag.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditPasswordRequestDTO {
    @NotBlank(message = "Field old password is mandatory!")
    private String oldPassword;
    @NotBlank(message = "Field new password is mandatory!")
    private String newPassword;
    @NotBlank(message = "Field confirm new password is mandatory!")
    private String confirmNewPassword;

}
