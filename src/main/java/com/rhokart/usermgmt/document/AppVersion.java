package com.rhokart.usermgmt.document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "app_version")
public class AppVersion {

    private String appId;
    private String appName;
    private String appVersion;
    private Boolean isActive;

}
