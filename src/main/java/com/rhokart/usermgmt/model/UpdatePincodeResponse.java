package com.rhokart.usermgmt.model;

import com.rhokart.datamodel.dto.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePincodeResponse extends ResponseData {
    private String message;
    private String userId;
    private String pincode;
}
