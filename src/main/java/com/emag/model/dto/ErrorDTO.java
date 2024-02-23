package com.emag.model.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDTO {

    private String message;
    private int status;
    private LocalDateTime time;
}
