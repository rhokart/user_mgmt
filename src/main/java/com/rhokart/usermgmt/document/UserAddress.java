package com.rhokart.usermgmt.document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddress {
    private String addressLine1;
    private String addressLine2;
    private String pincode;
    private String city;
    private String district;
    private String state;
}
