package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class UserNotFound extends RuntimeException {

    private String errDescription;
    private String errmessage;

    public UserNotFound(String errmessage) {
        this.errmessage = errmessage;
    }

    public UserNotFound(String errmessage, String errDescription) {
        this.errDescription = errDescription;
        this.errmessage = errmessage;
    }
}
