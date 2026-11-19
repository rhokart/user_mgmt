package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class OtpVerificationFailed extends RuntimeException {
    private String errDescription;
    private String errorMessage;

    public OtpVerificationFailed(String errorMessage, String errDescription) {
        this.errDescription = errDescription;
        this.errorMessage = errorMessage;
    }
}
