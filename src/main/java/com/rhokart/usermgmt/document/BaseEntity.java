package com.rhokart.usermgmt.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String updatedBy;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private Integer updatedAt;
    @JsonIgnore
    private Boolean isActive;
}
