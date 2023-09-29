package com.capgemini.demandforecast.service;

import com.capgemini.demandforecast.entity.Demand;
import com.capgemini.demandforecast.repository.DemandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandServiceImpl implements DemandService {

    @Autowired
    DemandRepository demandRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Demand> getDemands() {
        return demandRepository.findAll();
    }

    @Override
    public List<Demand> getDemandsByCustomer(String customer) {
        Query query = new Query();
        query.addCriteria(Criteria.where("customer").is(customer)).with(Sort.by(Sort.Direction.ASC, "deadline"));
        return mongoTemplate.find(query, Demand.class);
    }

    @Override
    public List<Demand> getDemandsBySkills(String[] skills) {
        Query query = new Query();
        query.addCriteria(Criteria.where("skills").in((Object) skills)).with(Sort.by(Sort.Direction.ASC, "deadline"));
        return mongoTemplate.find(query, Demand.class);
    }

    @Override
    public List<Demand> getDemandsPSUOnly(boolean isPSU) {
        Query query = new Query();
        query.addCriteria(Criteria.where("PSU").ne(null).exists(true)).with(Sort.by(Sort.Direction.ASC, "deadline"));
        return mongoTemplate.find(query, Demand.class);
    }
}
