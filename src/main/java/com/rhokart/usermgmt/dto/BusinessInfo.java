package com.rhokart.usermgmt.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessInfo {
    private String businessName;
    private String gstNumber;
    private String businessPan;
}
