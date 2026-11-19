package com.rhokart.usermgmt.dto;

import com.rhokart.datamodel.dto.ResponseData;
import com.rhokart.usermgmt.document.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUser extends ResponseData {
    private String userId;
    private String email;
    private String mobileNo;
    private String accessToken;
    private String refreshToken;
    private List<Role> roles;
    private boolean isProfileCompleted;
    private boolean isNewUser;
    private String defaultPincode;
    private String androidAppVersion;
    private String iOSAppVersion;
}
