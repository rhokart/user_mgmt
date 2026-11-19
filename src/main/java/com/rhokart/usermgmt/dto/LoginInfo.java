package com.rhokart.usermgmt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    @Valid
    private ApplicationContext applicationContext;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Please provide a valid email address.")
    private String email;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Pattern(regexp = "^\\d{10}$", message = "Please enter your 10 digit phone number")
    private String mobileNo;

    private String otp;
}
