package com.capgemini.demandforecast.rest;

import com.capgemini.demandforecast.entity.Demand;
import com.capgemini.demandforecast.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/demand-service", produces = "application/json")
public class DemandRestController {

    @Autowired
    private DemandService demandService;

    @GetMapping
    public List<Demand> getDemands() {
        return demandService.getDemands();
    }

    @GetMapping("/demands/customer/{customer}")
    public List<Demand> getDemandsByCustomer(@PathVariable String customer) {
        return  demandService.getDemandsByCustomer(customer);
    }

    @GetMapping("/demands/skills/{skills}")
    public List<Demand> getDemandsBySkills(@PathVariable String[] skills) {
        return  demandService.getDemandsBySkills(skills);
    }

    @GetMapping("/demands/isPSU/{isPSU}")
    public List<Demand> getDemandsPSUOnly(@PathVariable boolean isPSU) {
        return  demandService.getDemandsPSUOnly(isPSU);
    }
}
