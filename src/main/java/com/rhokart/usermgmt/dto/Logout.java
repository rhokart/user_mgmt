package com.rhokart.usermgmt.dto;

import com.rhokart.datamodel.dto.ResponseData;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Logout extends ResponseData {
    private Boolean isLogoutSuccessful;
    private String logoutMessage;
}
