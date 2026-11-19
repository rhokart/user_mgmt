package com.rhokart.usermgmt.repository;

import com.rhokart.usermgmt.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsById(String id);
}
