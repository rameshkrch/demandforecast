package com.capgemini.demandforecast.repository;

import com.capgemini.demandforecast.entity.Demand;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DemandRepository extends MongoRepository<Demand, String> {}
