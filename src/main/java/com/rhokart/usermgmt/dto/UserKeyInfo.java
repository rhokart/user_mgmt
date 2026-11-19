package com.rhokart.usermgmt.dto;

import com.rhokart.usermgmt.annotation.EmailOrMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@EmailOrMobile(message = "Email address incorrect.")
@NoArgsConstructor
@AllArgsConstructor
public class UserKeyInfo {

    @NotBlank
    private String applicationId;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Please provide a valid email address.")
    private String email;

    @Size(min = 10, max = 10, message = "Please enter your 10 digit phone number")
    @Pattern(regexp = "^\\d{10}$", message = "Please enter your 10 digit phone number")
    private String mobile;
}
