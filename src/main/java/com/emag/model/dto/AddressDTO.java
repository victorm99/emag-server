package com.emag.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddressDTO {
    @NotNull(message = "Address is mandatory!")
    private String address;
    private String description;

}
