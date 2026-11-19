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
public class UserInfo extends ResponseData {

    private String userId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String primaryMob;
    private String secondaryMob;
    private Address address;
    private BusinessInfo businessDetail;
    private String profilePictureUri;
    private Boolean profileCompleted;
}
