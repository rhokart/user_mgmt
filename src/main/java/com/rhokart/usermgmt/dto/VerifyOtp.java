package com.rhokart.usermgmt.dto;

import com.rhokart.usermgmt.model.OtpResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtp {
    private OtpResponse otpResponse;
    public String accessToken;
    public String refreshToken;
}
