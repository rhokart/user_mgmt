package com.rhokart.usermgmt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MobileOtp {


    @Valid
    private ApplicationContext applicationContext;

    @Size(min = 10, max = 10, message = "Please enter your 10 digit phone number")
    @Pattern(regexp = "^[0-9]*$", message = "Please enter your 10 digit phone number")
    private String mobNumber;
    @NotBlank(message = "Please enter your four digit OTP")
    @Size(min = 4, max = 4, message = "Please enter your four digit OTP")
    private String otp;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String fcmKey;
}
