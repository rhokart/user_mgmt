package com.rhokart.usermgmt.exceptation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private int errCode, errStatus;
    private String errMessage, errDescription;


    @Override
    public String toString() {
        return "ErrorMessage{" +
                "errCode=" + errCode +
                ", errStatus=" + errStatus +
                ", errMessage='" + errMessage + '\'' +
                ", errDescription='" + errDescription + '\'' +
                '}';
    }


}