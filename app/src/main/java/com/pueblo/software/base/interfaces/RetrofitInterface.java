package com.pueblo.software.base.interfaces;

import com.pueblo.software.base.dto.GetNewJobDTO;
import com.pueblo.software.base.dto.JobAssignDTO;
import com.pueblo.software.base.dto.JobDTO;
import com.pueblo.software.base.dto.LoginDTO;
import com.pueblo.software.base.dto.NewJobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @GET("/fucha/user/signin/{login}/{password}")
    Call<LoginDTO> doLogin(@Path("login") String login, @Path("password") String password);

    @GET("/fucha/job/getData/{login}")
    Call<GetNewJobDTO> getNewJobDTO(@Path("login")String login);

    @POST("/fucha/job/register")
    Call<String> postNewJobDTO(@Body NewJobDTO dto);

    @GET("/fucha/job/find/city/{city}/{login}")
    Call<List<JobDTO>> getJobsByCity(@Path("city")String city,@Path("login")String login);

    @POST("/fucha/job/associate")
    Call<String> postJobAssign(@Body JobAssignDTO dto);

    @GET("/fucha/job/getList/{login}")
    Call<List<JobDTO>> getAssignedJobs(@Path("login") String login);

    @POST("/fucha/job/done")
    Call<String> markJobAsDone(@Body String jobId);
}
