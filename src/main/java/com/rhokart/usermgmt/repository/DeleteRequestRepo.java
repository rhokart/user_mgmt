package com.rhokart.usermgmt.repository;

import com.rhokart.usermgmt.document.DeleteRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeleteRequestRepo extends MongoRepository<DeleteRequest,String> {
}
