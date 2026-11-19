package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class InvalidAppContext extends RuntimeException {
    private String errDescription;
    private String errmessage;

    public InvalidAppContext(String errmessage, String errDescription) {
        this.errDescription = errDescription;
        this.errmessage = errmessage;
    }
}
