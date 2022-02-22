package com.pueblo.software.base.common;

import com.pueblo.software.base.dto.GetNewJobDTO;
import com.pueblo.software.base.dto.JobDTO;
import com.pueblo.software.base.dto.LoginDTO;
import com.pueblo.software.base.models.Address;
import com.pueblo.software.base.models.JobType;

import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Retrofit;

public class CommonVariables {

    public static final String SERVER_URL ="http://192.168.43.185:8080/";
    public static final String ON_LOGIN_OK = "OK";
    public static final String ON_LOGIN_WRONG_CREDENTIALS = "WRONG_CREDENTIALS";
    public static final String DTO = "DTO";
    public static final String RESPONSE_OK = "OK";
    public static final String JOB_ALREADY_ASSIGNED = "JOB_ALREADY_ASSIGNED";
    public static final String ASSIGN_JOB = "ASSIGN_JOB";

    public static LoginDTO loginDTO;
    public static GetNewJobDTO getNewJobDTO;
    public static TreeMap<String,JobType> jobTypesTM;
    public static Address userAddress;

    public static byte[] avatar;
    public static List<JobDTO> myJobs;
}
