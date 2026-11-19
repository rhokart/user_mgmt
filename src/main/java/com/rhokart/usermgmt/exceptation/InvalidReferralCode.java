package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class InvalidReferralCode extends RuntimeException{

    private String errDescription;
    private String errmessage;


    public InvalidReferralCode(String errmessage, String errDescription) {
        this.errDescription = errDescription;
        this.errmessage = errmessage;
    }
}
