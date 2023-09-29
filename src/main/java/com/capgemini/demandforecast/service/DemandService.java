package com.capgemini.demandforecast.service;

import com.capgemini.demandforecast.entity.Demand;

import java.util.List;

public interface DemandService {
    List<Demand> getDemands();

    List<Demand> getDemandsByCustomer(String customer);

    List<Demand> getDemandsBySkills(String[] skills);

    List<Demand> getDemandsPSUOnly(boolean isPSU);
}
