package com.emag.model.dto.user;

import com.emag.model.pojo.Address;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Component
@Data
public class UserWithoutPasswordDTO {
    private long id;
    private String nickname;
    private String email;
    private String fullName;
    private Timestamp createdAt;
    private String imageUrl;
    private String mobilePhone;
    private boolean isAdmin;
    private LocalDate birthDate;
    private String gender;
    private List<Address> addresses;
    private boolean isSubscribed;



}
