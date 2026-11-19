package com.rhokart.usermgmt.dto;


import com.rhokart.datamodel.dto.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessToken extends ResponseData {
    private String userId;
    private String accessToken;
    private String returnCode;
    private String message;

}
