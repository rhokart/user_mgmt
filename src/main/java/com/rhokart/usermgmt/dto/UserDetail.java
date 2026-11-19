package com.rhokart.usermgmt.dto;

import com.rhokart.datamodel.dto.ResponseData;
import com.rhokart.usermgmt.document.BusinessDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetail extends ResponseData {
    private String userId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String primaryMob;
    private String secondaryMob;
    private Address address;
    private BusinessDetail businessDetail;
    private boolean isProfileCompleted;
    private String profilePictureUri;
}
