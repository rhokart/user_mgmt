package com.rhokart.usermgmt.repository;

import com.rhokart.usermgmt.document.OtpDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpDocRepo extends MongoRepository<OtpDoc, String> {
    OtpDoc findByUserKey(String userKey);
}
