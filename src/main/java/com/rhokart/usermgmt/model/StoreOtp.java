package com.rhokart.usermgmt.model;

import lombok.Data;

@Data
public class StoreOtp extends Object {
    private String mobileNo;
    private String otp;
    private int validityInSecond;
    private String generationTimeStamp;

    @Override
    public String toString() {
        return "StoreOtp{" +
                "mobileNo='" + mobileNo + '\'' +
                ", otp='" + otp + '\'' +
                ", validityInSecond=" + validityInSecond +
                ", generationTimeStamp='" + generationTimeStamp + '\'' +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
