package com.rhokart.usermgmt.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "otp_ref")
@NoArgsConstructor
public class OtpDoc extends BaseEntity {
    @Id
    private String userKey;

    private String otp;
    private long validityInSecond;
    private long generationTimeStamp;

    public OtpDoc(String userKey, String otp, long validityInSecond, long generationTimeStamp) {
        this.userKey = userKey;
        this.otp = otp;
        this.validityInSecond = validityInSecond;
        this.generationTimeStamp = generationTimeStamp;
    }
}
