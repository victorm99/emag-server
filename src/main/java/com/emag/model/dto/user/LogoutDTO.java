package com.emag.model.dto.user;

import lombok.Data;

@Data
public class LogoutDTO {
    private String message;

    public LogoutDTO(String message){
        this.message = message;
    }
}
