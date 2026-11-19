package com.rhokart.usermgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationContext {

    @NotBlank(message = "Application ID is Blank")
    private String applicationId;
    @NotBlank(message = "Application name is Blank")
    private String applicationName;

}
