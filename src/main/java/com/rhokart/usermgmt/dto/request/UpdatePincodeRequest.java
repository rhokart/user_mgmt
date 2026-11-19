package com.rhokart.usermgmt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePincodeRequest {
    private String userId;
    private String pincode;
}
