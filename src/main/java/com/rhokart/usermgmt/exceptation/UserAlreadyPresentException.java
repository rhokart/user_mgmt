package com.rhokart.usermgmt.exceptation;

import lombok.Data;

@Data
public class UserAlreadyPresentException extends RuntimeException {
    private String errDescription;
    private String errMessage;

    public UserAlreadyPresentException(String errMessage, String errDescription) {
        this.errDescription = errDescription;
        this.errMessage = errMessage;
    }
}
