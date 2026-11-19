package com.rhokart.usermgmt.document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDetail {

    private String businessName;
    private String gstNumber;
    private String businessPan;
}
