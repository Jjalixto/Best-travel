package com.joel.best_travel.domain.repositories.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.joel.best_travel.domain.entities.documents.AppUserDocument;

public interface AppUserRepository extends MongoRepository<AppUserDocument,String>{
    
    Optional<AppUserDocument> findByUsername(String username);
}
