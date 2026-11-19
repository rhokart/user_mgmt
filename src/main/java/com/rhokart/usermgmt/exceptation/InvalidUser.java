package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class InvalidUser extends RuntimeException {
    private String errDescription;
    private String errmessage;

    public InvalidUser(String errmessage, String errDescription) {
        this.errDescription = errDescription;
        this.errmessage = errmessage;
    }
}
