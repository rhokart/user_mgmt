package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class NotAbleSendSms extends Exception {
    private String errDescription;
    private String errmessage;

    public NotAbleSendSms() {
    }

    public NotAbleSendSms(String errmessage) {
        this.errmessage = errmessage;
    }

    public NotAbleSendSms(String errmessage, String errDescription) {
        this.errDescription = errDescription;
        this.errmessage = errmessage;
    }
}
