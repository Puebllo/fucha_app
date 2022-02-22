package com.pueblo.software.base.activites;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pueblo.software.base.common.CommonMethods;
import com.pueblo.software.base.common.CommonVariables;
import com.pueblo.software.base.dto.NewJobDTO;
import com.pueblo.software.base.interfaces.RetrofitInterface;
import com.pueblo.software.base.models.Address;
import com.pueblo.software.base.models.JobType;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.pueblo.com.fucha.R;

public class AddNewJobActivity extends AppCompatActivity {

    Spinner jobTypeSpinner;
    Button nextButton, addJobButton, selectDateButton,backToHomeActivity;
    EditText jobNameTextEdit, jobDescriptionTextEdit, expireDateTextEdit, costTextEdit, streetTextEdit, buildingNrTextEdit, flatNrTextEdit, cityTextEdit, postalCodeTextEdit;
    int layoutTag;
    DatePickerDialog.OnDateSetListener dpListener;
    LocalDate expireDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_job_layout_1);
        layoutTag =R.layout.add_new_job_layout_1;

        //Strona 1
        jobNameTextEdit = findViewById(R.id.job_name_edit_text);
        jobTypeSpinner = findViewById(R.id.job_type_spinner);
        jobDescriptionTextEdit = findViewById(R.id.job_description_edit_text);
        nextButton = findViewById(R.id.next_button);

        List<String> itemList = getItemListFromDTO(CommonVariables.jobTypesTM);

        ArrayAdapter<String> adapter =new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTypeSpinner.setAdapter(adapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    setContentView(R.layout.add_new_job_layout_2);
                    layoutTag =R.layout.add_new_job_layout_2;
                    expireDateTextEdit =findViewById(R.id.expire_date_edit_text);
                    costTextEdit = findViewById(R.id.cost_edit_text);
                    streetTextEdit = findViewById(R.id.street_text_view);
                    buildingNrTextEdit = findViewById(R.id.building_nr_text_view);
                    flatNrTextEdit = findViewById(R.id.flat_nr_text_view);
                    cityTextEdit = findViewById(R.id.city_text_view);
                    postalCodeTextEdit = findViewById(R.id.postal_code_text_view);
                    addJobButton = findViewById(R.id.add_job_button);
                    selectDateButton = findViewById(R.id.select_date_button);

                    selectDateButton.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View view) {
                            LocalDate ldt = LocalDate.now();
                            DatePickerDialog dialog = new DatePickerDialog(AddNewJobActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,dpListener,ldt.getYear(),ldt.getMonthValue()-1,ldt.getDayOfMonth());
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
                            dialog.show();
                        }
                    });
                    dpListener = new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            month++;
                            expireDateTextEdit.setError(null);
                            expireDateTextEdit.setText(day +"."+month+"."+year);
                            expireDate = LocalDate.of(year,month,day);
                        }
                    };

                    addJobButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(checkFields()){
                                Address address = getAddressFromFields();
                                geocodeAddress(address);
                                NewJobDTO dto = new NewJobDTO();
                                dto.setJobName(jobNameTextEdit.getText().toString());
                                dto.setJobType(CommonVariables.jobTypesTM.get(jobTypeSpinner.getSelectedItem()));
                                dto.setJobDescription(jobDescriptionTextEdit.getText().toString());
                                dto.setExpireLocalDate(expireDate.toString());
                                dto.setCost(Double.parseDouble(costTextEdit.getText().toString()));
                                dto.setAddress(address);
                                dto.setUserLogin(CommonVariables.loginDTO.getLogin());

                                RetrofitInterface retrofitInterface = CommonMethods.getRetrofitInterface();
                                Call<String> call = retrofitInterface.postNewJobDTO(dto);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if(response.isSuccessful()){
                                            if(response.body().equals(CommonVariables.RESPONSE_OK)){
                                                setContentView(R.layout.add_new_job_layout_message);
                                                backToHomeActivity = findViewById(R.id.back_to_home_activity_button);
                                                backToHomeActivity.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(AddNewJobActivity.this,HomeMapActivity.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(AddNewJobActivity.this,R.string.something_went_wrong_msg, Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
            }

    private void geocodeAddress(Address address) {
        Geocoder gc = new Geocoder(AddNewJobActivity.this);
        try {
            List<android.location.Address> addressList = gc.getFromLocationName(address.toString(),1);
            if(addressList!=null && addressList.size()>0){
                android.location.Address gcAddress = addressList.get(0);
                address.setLat(gcAddress.getLatitude());
                address.setLng(gcAddress.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Address getAddressFromFields() {
        Address toReturn = new Address();
        toReturn.setStreet(streetTextEdit.getText().toString());
        toReturn.setBuildingNr(buildingNrTextEdit.getText().toString());
        if(flatNrTextEdit.getText().length()>0){
            toReturn.setFlatNr(flatNrTextEdit.getText().toString());
        }
        toReturn.setCity(cityTextEdit.getText().toString());
        toReturn.setPostalCode(postalCodeTextEdit.getText().toString());
        
        return toReturn;
    }

    private List<String> getItemListFromDTO(TreeMap<String,JobType> jobTypes) {
        List<String> toReturn = new ArrayList<>();

        for(String jobName : jobTypes.keySet()){
            toReturn.add(jobName);
        }
        return toReturn;
    }


    private boolean checkFields() {
        switch (layoutTag){
            case R.layout.add_new_job_layout_1:
                if(jobNameTextEdit.getText().length()==0){
                    jobNameTextEdit.setError(getResources().getString(R.string.fill_job_name_field));
                    return false;
                }
                if(jobDescriptionTextEdit.getText().length()==0) {
                    jobDescriptionTextEdit.setError(getResources().getString(R.string.fill_job_decription_field));
                    return false;
                }
                break;
            case R.layout.add_new_job_layout_2:
                if(expireDate ==null){
                    expireDateTextEdit.setError(getResources().getString(R.string.fill_job_name_field));
                    return false;
                }
                if(costTextEdit.getText().length()==0) {
                    costTextEdit.setError(getResources().getString(R.string.fill_job_decription_field));
                    return false;
                }
                if(streetTextEdit.getText().length()==0) {
                    streetTextEdit.setError(getResources().getString(R.string.fill_job_decription_field));
                    return false;
                }
                if(buildingNrTextEdit.getText().length()==0) {
                    buildingNrTextEdit.setError(getResources().getString(R.string.fill_job_decription_field));
                    return false;
                }
                if(cityTextEdit.getText().length()==0) {
                    cityTextEdit.setError(getResources().getString(R.string.fill_job_decription_field));
                    return false;
                }
                if(postalCodeTextEdit.getText().length()==0) {
                    postalCodeTextEdit.setError(getResources().getString(R.string.fill_job_decription_field));
                    return false;
                }
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(layoutTag == R.layout.add_new_job_layout_2){
            layoutTag = R.layout.add_new_job_layout_1;
            setContentView(R.layout.add_new_job_layout_1);
        }else{
            super.onBackPressed();
        }
    }
}


