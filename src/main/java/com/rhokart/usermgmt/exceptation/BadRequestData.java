package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class BadRequestData extends RuntimeException {
    private String errDescription;
    private String errmessage;

    public BadRequestData(String errmessage, String errDescription) {
        this.errDescription = errDescription;
        this.errmessage = errmessage;
    }
}
