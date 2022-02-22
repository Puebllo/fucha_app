package com.pueblo.software.base.dto;

import com.pueblo.software.base.models.Address;
import com.pueblo.software.base.models.JobType;

import java.io.Serializable;
import java.time.LocalDate;

public class JobDTO implements Serializable {

    private static final long serialVersionUID = 7987104867724519825L;

    public Long id;
    public JobType jobType;
    public String jobName;
    public String jobDescription;
    public String jobExpireDate;
    public Double cost;
    public Address address;
    public boolean isFinished;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public JobType getJobType() {
        return jobType;
    }
    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

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

    public String getJobExpireDate() {
        return jobExpireDate;
    }
    public void setJobExpireDate(String jobExpireDate) {
        this.jobExpireDate = jobExpireDate;
    }

    public Double getCost() {
        return cost;
    }
    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isFinished() {
        return isFinished;
    }
    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
