package com.pueblo.software.base.common;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.pueblo.software.base.enums.JobEnum;
import com.pueblo.software.base.interfaces.RetrofitInterface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import software.pueblo.com.fucha.R;

public class CommonMethods {

    public static RetrofitInterface retrofitInterface;

    public static Retrofit buildRetrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(CommonVariables.SERVER_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());


        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static RetrofitInterface getRetrofitInterface(){
        if(retrofitInterface==null){
            Retrofit retrofit = buildRetrofit();
            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
        return retrofitInterface;
    }

    public static int getImageByJobType(JobEnum jobEnum) {
        switch(jobEnum){
            case IT:
                return R.drawable.it;
            case PROGRAMMER:
                return R.drawable.programmer;
            case PLUMBER:
                return R.drawable.plumber;
            case ELECTRICIAN:
                return R.drawable.electric;
            case CARPENTER:
                return R.drawable.capeneter;
            case CAR_MECHANIC:
                return R.drawable.car_mechanic;
        }
        return R.drawable.it;
    }

    public static byte[] toByteArray(short[] buf)
    {
        int size = buf.length;
        byte[] toReturn = new byte[size];

        for (int index = 0; index < size; index++)   {
            toReturn[index] = (byte) buf[index];
        }
        return toReturn;
    }

    public static BitmapDescriptor getMarkerColorByJobType(JobEnum jobEnum) {

        switch (jobEnum){
            case IT:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            case PLUMBER:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
            case CARPENTER:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
            case PROGRAMMER:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
            case ELECTRICIAN:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            case CAR_MECHANIC:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
        }
        return null;
    }

    public static void clearCache() {
        CommonVariables.loginDTO = null;
        CommonVariables.getNewJobDTO = null;
        CommonVariables.jobTypesTM = null;
        CommonVariables.userAddress = null;
        CommonVariables.avatar = null;
        CommonVariables.myJobs=null;
    }
}
