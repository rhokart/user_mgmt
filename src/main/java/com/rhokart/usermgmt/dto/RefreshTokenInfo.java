package com.rhokart.usermgmt.dto;

import lombok.Data;

@Data
public class RefreshTokenInfo {
    private String refreshToken;
    private String userId;
}
