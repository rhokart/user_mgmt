package com.rhokart.usermgmt.repository;

import com.rhokart.usermgmt.document.DeletedUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeletedUserRepository extends MongoRepository<DeletedUser, String> {
}

