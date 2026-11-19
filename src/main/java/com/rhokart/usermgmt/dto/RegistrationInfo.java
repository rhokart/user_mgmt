package com.rhokart.usermgmt.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationInfo {
    @Valid
    private ApplicationContext applicationContext;

    private String userId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    @Size(min = 10, max = 10)
    private String primaryMob;
    @Size(min = 10, max = 10)
    private String secondaryMob;
    private Address userAddress;
    private BusinessInfo businessInfo;
}
