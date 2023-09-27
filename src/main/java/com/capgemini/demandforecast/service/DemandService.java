package com.capgemini.demandforecast.service;

import com.capgemini.demandforecast.entity.Demand;

import java.util.List;
import java.util.Objects;

public interface DemandService {
    List<Demand> getDemands();

    List<Demand> getDemandsByCustomer(String customer);

    List<Demand> getDemandsBySkills(List<Objects> skills);

    List<Demand> getDemandsPSUOnly(boolean isPSU);
}
