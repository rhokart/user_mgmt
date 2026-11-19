package com.rhokart.usermgmt.repository;

import com.rhokart.usermgmt.document.AppVersion;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AppVersionRepo extends MongoRepository<AppVersion, String> {

}
