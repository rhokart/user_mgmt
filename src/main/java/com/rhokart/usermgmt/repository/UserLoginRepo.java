package com.rhokart.usermgmt.repository;

import com.rhokart.usermgmt.document.UserLogin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepo extends MongoRepository<UserLogin,String> {
}
