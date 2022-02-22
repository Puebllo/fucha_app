package com.pueblo.software.base.dto;

import com.pueblo.software.base.models.Address;
import com.pueblo.software.base.models.JobType;

import java.io.Serializable;
import java.util.List;

public class GetNewJobDTO implements Serializable {


    private static final long serialVersionUID = -7994608939809407752L;
    Address address;
    List<JobType> jobTypes;

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    public List<JobType> getJobTypes() {
        return jobTypes;
    }
    public void setJobTypes(List<JobType> jobTypes) {
        this.jobTypes = jobTypes;
    }

}