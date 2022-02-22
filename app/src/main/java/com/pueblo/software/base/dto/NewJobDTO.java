package com.pueblo.software.base.dto;

import com.pueblo.software.base.models.Address;
import com.pueblo.software.base.models.JobType;

import java.io.Serializable;
import java.time.LocalDate;

public class NewJobDTO implements Serializable {

    private static final long serialVersionUID = -5438599979075486436L;

    String jobName;
    String jobDescription;
    JobType jobType;
    Double cost;
    String expireLocalDate;

    String userLogin;
    Address address;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getExpireLocalDate() {
        return expireLocalDate;
    }

    public void setExpireLocalDate(String expireLocalDate) {
        this.expireLocalDate = expireLocalDate;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


}
