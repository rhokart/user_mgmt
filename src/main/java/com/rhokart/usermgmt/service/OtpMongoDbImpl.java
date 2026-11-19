package com.rhokart.usermgmt.service;

import com.rhokart.usermgmt.document.OtpDoc;
import com.rhokart.usermgmt.repository.OtpDocRepo;
import com.rhokart.usermgmt.util.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
@Slf4j
public class OtpMongoDbImpl {

    @Autowired
    private OtpDocRepo otpDocRepo;


    public String generateOtp(String mobNumber, int len) {
        String validNumber = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            otp.append(validNumber.charAt(random.nextInt(validNumber.length())));

        log.info("generateOtp--  Mob={},Opt={}", mobNumber, otp);
        return otp.toString();
    }


    public boolean validateOtp(String userKey, String oneTimePass) {

        if(userKey==null || oneTimePass==null)
            return Boolean.FALSE;

        //Test user creation and validation for login.
        if(userKey.equalsIgnoreCase("testrhokart@gmail.com") && oneTimePass.equalsIgnoreCase("54321"))
            return Boolean.TRUE;


        OtpDoc retrieveOneTimePassword = otpDocRepo.findByUserKey(userKey);
        log.info("For userKey ::{}, Otp Object ::{}", userKey, retrieveOneTimePassword);
        if (retrieveOneTimePassword == null)
            return Boolean.FALSE;

        long currentTime = System.currentTimeMillis();
        long diffTime = (currentTime - retrieveOneTimePassword.getGenerationTimeStamp()) / 1000;
        log.info("time validity remaining for login otp::{}", diffTime);

        if (oneTimePass.equalsIgnoreCase(retrieveOneTimePassword.getOtp())) {
            if ((diffTime <= GlobalConstants.OTP_VALIDITY)) {
                otpDocRepo.delete(retrieveOneTimePassword);
                return Boolean.TRUE;
            }
        }
        return  Boolean.FALSE;
    }
}

