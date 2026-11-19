package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class IncomingPayloadException extends RuntimeException {
    private static final long serialVersionUID = -470180507998010368L;
    private String errDescription;
    private String errmessage;

    public IncomingPayloadException(String errmessage, String errDescription) {
        this.errDescription = errDescription;
        this.errmessage = errmessage;
    }
}
