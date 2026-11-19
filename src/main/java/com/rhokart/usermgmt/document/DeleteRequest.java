package com.rhokart.usermgmt.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "delete_request")
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRequest {
    private String name;
    private String email;
    private String mobile;
}
