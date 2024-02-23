package com.emag.model.dto.register;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;

@Data
@Component
public class RegisterResponseUserDTO {
    private long id;
    private String email;
    private String fullName;
//    @Column(name = "is_admin")
    private boolean isAdmin;
}
