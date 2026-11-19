package com.rhokart.usermgmt.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutInfo {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String userId;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String appId;


    @NotBlank(message = "Missing Token, Unable to Logout")
    private String refreshToken;
}
