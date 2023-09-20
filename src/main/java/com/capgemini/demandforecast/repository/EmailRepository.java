package com.capgemini.demandforecast.repository;

import com.capgemini.demandforecast.entity.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Email, String> {}
