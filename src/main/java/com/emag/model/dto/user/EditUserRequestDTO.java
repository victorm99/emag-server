package com.emag.model.dto.user;

import com.emag.model.dto.AddressDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EditUserRequestDTO {
    private String mobilePhone;
    private AddressDTO address;
    private String nickname;
    private String gender;
    private LocalDate birthDate;
    private String email;


}
