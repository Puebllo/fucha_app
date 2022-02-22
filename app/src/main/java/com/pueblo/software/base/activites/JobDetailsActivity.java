package com.pueblo.software.base.activites;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pueblo.software.base.common.CommonMethods;
import com.pueblo.software.base.common.CommonVariables;
import com.pueblo.software.base.dto.JobAssignDTO;
import com.pueblo.software.base.dto.JobDTO;
import com.pueblo.software.base.interfaces.RetrofitInterface;
import com.pueblo.software.base.models.Address;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.pueblo.com.fucha.R;

public class JobDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    JobDTO dto;
    ImageView jobTypeImage;
    TextView jobNameTextView,jobTypeTextView,jobDescriptionTextView,costTextView,expireDateTextView,addressTextView;
    Button takeTheJobButton;
    private SupportMapFragment mapFragment;
    private Address address;
    private boolean assignJob;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.job_detail_map_fragment);
            mapFragment.getMapAsync(this);

            dto = (JobDTO)extras.get(CommonVariables.DTO);
            address = dto.getAddress();
            jobNameTextView = findViewById(R.id.job_details_job_name_tv);
            jobTypeTextView = findViewById(R.id.job_details_job_type_tv);
            jobDescriptionTextView =findViewById(R.id.job_details_job_description_tv);
            costTextView = findViewById(R.id.cost_value_text_view);
            expireDateTextView = findViewById(R.id.job_details_expire_date);
            addressTextView = findViewById(R.id.job_details_address_to_string);
            takeTheJobButton = findViewById(R.id.take_job_button);
            jobTypeImage = findViewById(R.id.job_details_image_view);

            jobNameTextView.setText(dto.getJobName());
            jobTypeTextView.setText(dto.getJobType().getJobName());
            jobDescriptionTextView.setText(dto.getJobDescription());
            costTextView.setText(dto.getCost().toString() + "zł");
            expireDateTextView.setText(String.format(dto.getJobExpireDate(), DateTimeFormatter.ISO_LOCAL_DATE));
            addressTextView.setText(dto.getAddress().toString());

            assignJob = (boolean) extras.get(CommonVariables.ASSIGN_JOB);
            if(assignJob){
                takeTheJobButton.setText(getResources().getString(R.string.take_job));
            }
            else{
                takeTheJobButton.setText(getResources().getString(R.string.mark_job_as_done));
                if(dto.isFinished()){
                    takeTheJobButton.setEnabled(false);
                    takeTheJobButton.setBackgroundColor(getColor(R.color.inactive_color));
                }
            }

            jobTypeImage.setImageResource(CommonMethods.getImageByJobType(dto.getJobType().getJobEnum()));

            takeTheJobButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(assignJob){
                        assignJobForMe();
                    }else{
                        markJobAsDone();
                    }

                }
            });

        }




    }

    private void markJobAsDone() {
            RetrofitInterface retrofitInterface = CommonMethods.getRetrofitInterface();
            Call<String> call = retrofitInterface.markJobAsDone(dto.getId().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        if(response.body().equals(CommonVariables.RESPONSE_OK)){
                            dto.setFinished(true);
                            Toast.makeText(JobDetailsActivity.this,getResources().getString(R.string.job_has_been_marked_as_finished),Toast.LENGTH_LONG).show();
                            Intent homeIntent = new Intent(JobDetailsActivity.this, HomeMapActivity.class);
                            startActivity(homeIntent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e (this.getClass().getSimpleName(),t.getMessage());
                }
            });

        }

    private void assignJobForMe() {
        RetrofitInterface retrofitInterface = CommonMethods.getRetrofitInterface();
        JobAssignDTO jaDTO = new JobAssignDTO();
        jaDTO.setJobId(dto.getId());
        jaDTO.setUserLogin(CommonVariables.loginDTO.getLogin());

        Call<String> call = retrofitInterface.postJobAssign(jaDTO);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()){
                    Log.e (this.getClass().getSimpleName(),response.body());

                    if(response.body().equals(CommonVariables.RESPONSE_OK)){
                        Toast.makeText(JobDetailsActivity.this,getResources().getString(R.string.job_has_been_added),Toast.LENGTH_LONG).show();
                        //TODO: jakis blip na menu
                    }
                    if(response.body().equals(CommonVariables.JOB_ALREADY_ASSIGNED)){
                        Toast.makeText(JobDetailsActivity.this,getResources().getString(R.string.job_is_already_assigned),Toast.LENGTH_LONG).show();
                        //TODO: usuwanie takiego markera
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e (this.getClass().getSimpleName(),t.getMessage());

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(address.getLat()!=null && address.getLng()!=null){
            LatLng sydney = new LatLng(address.getLat(), address.getLng());
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Tutaj"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
        }
    }
}
