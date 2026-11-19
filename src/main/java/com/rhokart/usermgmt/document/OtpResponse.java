package com.rhokart.usermgmt.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rhokart.datamodel.dto.CoreData;
import com.rhokart.datamodel.dto.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpResponse extends ResponseData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mobileNo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    private long validityInSecond;
    private long generationTimeStamp;
}
