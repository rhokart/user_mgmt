package com.rhokart.usermgmt.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    private String givenName;
    private String familyName;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String profilePictureUri;
}
