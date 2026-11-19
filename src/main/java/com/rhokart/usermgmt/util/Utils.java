package com.rhokart.usermgmt.util;

import com.rhokart.usermgmt.enums.ApplicationId;
import com.rhokart.usermgmt.enums.ApplicationName;
import com.rhokart.usermgmt.exceptation.InvalidAppContext;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static Boolean validateApplicationContext(String applicationId) {
        try {
            List<ApplicationId> appNames = Arrays.asList(ApplicationId.values());
            return appNames.contains(ApplicationId.valueOf(applicationId.toUpperCase()));
        } catch (Exception e) {
            throw new InvalidAppContext("Invalid Application Context",
                    "Please Check AppId or AppName.");
        }
    }

    public static Boolean validateApplicationContext(String applicationId, String applicationName) {
        try {
            List<ApplicationId> appIds = Arrays.asList(ApplicationId.values());
            Boolean appId = appIds.contains(ApplicationId.valueOf(applicationId.toUpperCase()));

            List<ApplicationName> applicationNames = Arrays.asList(ApplicationName.values());
            Boolean appName = applicationNames.contains(ApplicationName.valueOf(applicationName.toUpperCase()));

            if (appId && appName) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            throw new InvalidAppContext("Invalid Application Context",
                    "Please Check AppId or AppName.");
        }
    }

    public static String generateUserId(){
        long epocTime = Instant.now().toEpochMilli();   //user ID is like of epoc time in millisecond which will be constant for all time.
        return String.valueOf(epocTime);
    }
}
