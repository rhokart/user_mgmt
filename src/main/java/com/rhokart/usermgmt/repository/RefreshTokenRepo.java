package com.rhokart.usermgmt.repository;

import com.rhokart.usermgmt.document.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepo extends MongoRepository<RefreshToken, String> {
}
