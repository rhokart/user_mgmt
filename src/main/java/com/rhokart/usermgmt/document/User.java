package com.rhokart.usermgmt.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Document(collection = "user_mgmt")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String gender;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    @Size(min = 10, max = 10)
    private String primaryMob;
    @Size(min = 10, max = 10)
    private String secondaryMob;
    private UserAddress userAddress;
    private BusinessDetail businessDetail;
    private boolean isProfileCompleted;
    private String profilePictureUri;
    private List<Role> roles;
    private String defaultPincode;
}
