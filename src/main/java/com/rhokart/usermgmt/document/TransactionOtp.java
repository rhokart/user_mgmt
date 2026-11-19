package com.rhokart.usermgmt.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class TransactionOtp {
    @Id
    private String mobileNo;

    private String otp;
    private long validityInSecond;
    private long generationTimeStamp;

    public TransactionOtp(String mobileNo, String otp, long validityInSecond, long generationTimeStamp) {
        this.mobileNo = mobileNo;
        this.otp = otp;
        this.validityInSecond = validityInSecond;
        this.generationTimeStamp = generationTimeStamp;
    }
}
