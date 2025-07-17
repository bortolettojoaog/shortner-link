package com.bortolettotech.shortner_link.repository;

import com.bortolettotech.shortner_link.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LinkRepositoryI extends MongoRepository<Link, String> {
    Optional<Link> findByShortCode(String shortCode);
}
