package com.pueblo.software.base.models;

import com.pueblo.software.base.enums.JobEnum;
import com.pueblo.software.base.enums.LanguageCodeEnum;

import java.io.Serializable;

public class JobType implements Serializable {

    private static final long serialVersionUID = -5062547139350491229L;


    public Long id;
    public JobEnum jobEnum;
    public String jobName;
    public LanguageCodeEnum languageCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobEnum getJobEnum() {
        return jobEnum;
    }

    public void setJobEnum(JobEnum jobEnum) {
        this.jobEnum = jobEnum;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


    public LanguageCodeEnum getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(LanguageCodeEnum languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return jobEnum.name() + ": " + jobName + "[" + languageCode.name() + "]";
    }
}