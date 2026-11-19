package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class InvalidToken extends RuntimeException {
    private String errDescription;
    private String errmessage;


    public InvalidToken(String errmessage, String errDescription) {
        this.errDescription = errDescription;
        this.errmessage = errmessage;
    }
}
