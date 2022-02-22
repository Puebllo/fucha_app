package com.pueblo.software.base.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pueblo.software.base.common.CommonMethods;
import com.pueblo.software.base.common.CommonVariables;
import com.pueblo.software.base.dto.GetNewJobDTO;
import com.pueblo.software.base.dto.JobDTO;
import com.pueblo.software.base.interfaces.RetrofitInterface;
import com.pueblo.software.base.layouts.MapWrapperLayout;
import com.pueblo.software.base.layouts.OnInfoWindowElemTouchListener;
import com.pueblo.software.base.models.JobType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import software.pueblo.com.fucha.R;

public class HomeMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ViewGroup infoWindow;
    private TextView jobTitle;
    private TextView jobType;
    private TextView jobDetails;
    private Button detailsButton;
    private ImageView jobImage;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private DrawerLayout menu;
    private ImageView avatar;
    private FloatingActionsMenu menuMultipleActions;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    HashMap<Long,JobDTO> jobHM = new HashMap<>();

    private TextView menuHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        toolbar.setTitle(R.string.jobs_in_your_area);
        setSupportActionBar(toolbar);
       // toolbar.inflateMenu(R.menu.toolbar_menu);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        menu = findViewById(R.id.drawer_layout);


        FloatingActionButton buttonA = findViewById(R.id.action_a);
        buttonA.setIcon(R.drawable.ic_add_jom_24dp);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuMultipleActions.collapse();
                getNewJobDTO();

            }
        });

        menuMultipleActions = findViewById(R.id.multiple_actions);

        menuListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menu.openDrawer(GravityCompat.START);
                return true;
            case R.id.toolbar_menu_refresh:
                refreshMap();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshMap() {
        googleMap.clear();
        getJobsForCity(googleMap,"Jelenia Góra");
        moveCameraToYoursCity(googleMap);
    }


    private void menuListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()){
                    case R.id.menu_item_my_account:
                        return true;
                    /*case R.id.menu_item_add_job:
                        menu.closeDrawers();
                        // if(CommonVariables.getNewJobDTO==null){
                        getNewJobDTO();
                        //  }
                        menuItem.setChecked(false);
                        Intent newJobIntent = new Intent(HomeMapActivity.this,AddNewJobActivity.class);
                        startActivity(newJobIntent);

                        return true;*/
                    case R.id.menu_item_job_history:
                        menuItem.setChecked(false);
                        goToMyJobActivity();
                        return true;
                    case R.id.menu_item_settings:

                        return true;
                    case R.id.menu_item_logout:
                        menuItem.setChecked(false);
                        doLogout();
                        return true;
                }
                return false;
            }
        });

        menu.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                menuHeader = findViewById(R.id.menu_login_text_view);
                menuHeader.setText(getResources().getString(R.string.header_welcome) + " " + CommonVariables.loginDTO.getLogin() + " !");

                avatar = findViewById(R.id.profile_image);
                byte[] byteArray = CommonVariables.avatar;
                if(byteArray!=null){
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    if(bmp!=null) {
                        avatar.setImageBitmap(Bitmap.createScaledBitmap(bmp, avatar.getWidth(),
                                avatar.getHeight(), false));
                    }
                }

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }

    private void doLogout() {
        CommonMethods.clearCache();
        Intent myJobIntent = new Intent(HomeMapActivity.this,MainActivity.class);
        startActivity(myJobIntent);
    }


    private void goToMyJobActivity() {
        RetrofitInterface retrofitInterface = CommonMethods.getRetrofitInterface();

        Call<List<JobDTO>> call = retrofitInterface.getAssignedJobs(CommonVariables.loginDTO.getLogin());
        call.enqueue(new Callback<List<JobDTO>>() {
            @Override
            public void onResponse(Call<List<JobDTO>> call, Response<List<JobDTO>> response) {
                if(response.isSuccessful()){
                    Intent myJobIntent = new Intent(HomeMapActivity.this,MyJobActivity.class);
                    CommonVariables.myJobs = response.body();
                    startActivity(myJobIntent);
                }
            }

            @Override
            public void onFailure(Call<List<JobDTO>> call, Throwable t) {

            }
        });

    }

    private void getNewJobDTO() {

        RetrofitInterface retrofitInterface = CommonMethods.getRetrofitInterface();
        Call<GetNewJobDTO> call = retrofitInterface.getNewJobDTO(CommonVariables.loginDTO.getLogin());

        call.enqueue(new Callback<GetNewJobDTO>() {
            @Override
            public void onResponse(Call<GetNewJobDTO> call, Response<GetNewJobDTO> response) {
                if(response.isSuccessful()) {
                    CommonVariables.getNewJobDTO = response.body();

                    TreeMap<String,JobType> tempTM = new TreeMap<>();
                    for(JobType jobType : CommonVariables.getNewJobDTO.getJobTypes()){
                        tempTM.put(jobType.getJobName(),jobType);
                    }
                    CommonVariables.jobTypesTM = tempTM;

                    CommonVariables.userAddress =  CommonVariables.getNewJobDTO.getAddress();

                    Intent newJobIntent = new Intent(HomeMapActivity.this,AddNewJobActivity.class);
                    startActivity(newJobIntent);
                }
            }
            @Override
            public void onFailure(Call<GetNewJobDTO> call, Throwable t) {
                Toast.makeText(HomeMapActivity.this,R.string.something_went_wrong_msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        final MapWrapperLayout mapWrapperLayout = findViewById(R.id.map_relative_layout);

        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(googleMap, getPixelsFromDp(this, 39 + 20));

        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.marker_details, null);

        this.jobTitle = infoWindow.findViewById(R.id.job_details_job_name_tv);
        this.jobType = infoWindow.findViewById(R.id.job_details_job_type_tv);
        this.jobDetails = infoWindow.findViewById(R.id.job_details_job_description_tv);
        this.detailsButton = infoWindow.findViewById(R.id.marker_details_see_more_button);
        this.jobImage = infoWindow.findViewById(R.id.marker_details_image_view);
        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        /*this.infoButtonListener = new OnInfoWindowElemTouchListener(jobDetails*//*, getResources().getDrawable(R.drawable.cast_abc_scrubber_control_to_pressed_mtrl_005), getResources().getDrawable(R.drawable.cast_abc_scrubber_control_to_pressed_mtrl_005)*//*){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
               // Toast.makeText(HomeMapActivity.this, "click on button 1", Toast.LENGTH_SHORT).show();
            }
        };
        this.jobDetails.setOnTouchListener(infoButtonListener);*/

        infoButtonListener = new OnInfoWindowElemTouchListener(detailsButton/*, getResources().getDrawable(R.drawable.cast_abc_scrubber_control_to_pressed_mtrl_005),getResources().getDrawable(R.drawable.cast_abc_scrubber_control_to_pressed_mtrl_005)*/){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                JobDTO dto = jobHM.get(Long.parseLong(marker.getTitle()));
                Intent jobDetailsIntent = new Intent(HomeMapActivity.this,JobDetailsActivity.class);
                jobDetailsIntent.putExtra(CommonVariables.DTO, dto);
                jobDetailsIntent.putExtra(CommonVariables.ASSIGN_JOB, true);

                startActivity(jobDetailsIntent);
            }
        };
        detailsButton.setOnTouchListener(infoButtonListener);

        /*infoWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "click on infowindow", Toast.LENGTH_LONG).show();
            }
        });*/

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                final JobDTO dto = jobHM.get(Long.parseLong(marker.getTitle()));
                // Setting up the infoWindogw with current's marker info
                jobTitle.setText(dto.getJobName());
                jobType.setText(dto.getJobType().getJobName());
                jobDetails.setText(dto.jobDescription);
                jobImage.setImageResource(CommonMethods.getImageByJobType(dto.getJobType().getJobEnum()));
                infoButtonListener.setMarker(marker);


                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        getJobsForCity(googleMap,"Jelenia Góra");
/*
        for(JobDTO dto : jobHM.values()){
           LatLng latlng = new LatLng(dto.getAddress().getLat(), dto.getAddress().getLng());

            googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(dto.getId().toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }*/
        // Let's add a couple of markers



        moveCameraToYoursCity(googleMap);

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng1, 10));
    }

    private void moveCameraToYoursCity(GoogleMap googleMap) {
        Geocoder gc = new Geocoder(this);
        try {
            //TODO: tutaj podstawiac miasto uzytkownika
            List<Address> addresses= gc.getFromLocationName("Jelenia Góra", 1);
            if(addresses!=null && addresses.size()>=1){
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getJobsForCity(final GoogleMap googleMap, String city) {
        List<JobDTO> toReturn = new ArrayList<>();
        RetrofitInterface retrofitInterface = CommonMethods.getRetrofitInterface();
        Call<List<JobDTO>> call = retrofitInterface.getJobsByCity(city,CommonVariables.loginDTO.login);
        call.enqueue(new Callback<List<JobDTO>>() {
            @Override
            public void onResponse(Call<List<JobDTO>> call, Response<List<JobDTO>> response) {
                if(response.isSuccessful()){
                    for(JobDTO dto : response.body()){
                        jobHM.put(dto.getId(),dto);

                        LatLng latlng = new LatLng(dto.getAddress().getLat(), dto.getAddress().getLng());

                        googleMap.addMarker(new MarkerOptions()
                                .position(latlng)
                                .title(dto.getId().toString())
                                .icon(CommonMethods.getMarkerColorByJobType(dto.getJobType().getJobEnum())));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<JobDTO>> call, Throwable t) {

            }
        });
    }


}
