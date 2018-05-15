package com.evt.dmp.protocal;

import com.evt.dmp.protocal.dto.NewAdmin;
import com.evt.dmp.protocal.dto.PlanItem;
import com.evt.dmp.protocal.dto.PlanItemRoot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by everitime5 on 2018-01-22.
 */

public interface DmpWebService {
    public static final String API_URL = "http://52.79.54.80:9000/dmp/";
    //public static final String API_URL = "http://192.168.10.56:1338/dmp/";

    @POST("plan")
    Call<ArrayList<PlanItem>> setPlan(@Body ArrayList<PlanItem> planItem);

    @GET("plan")
    Call<PlanItemRoot> getAllPlan();

    @GET("plan")
    Call<PlanItemRoot> getPlan(
            @Query("id") String id, @Query("date") String date
    );

    @POST("new")
    Call<NewAdmin> setAdmin(@Body NewAdmin newAdmin);

    @GET("login")
    Call<ArrayList<NewAdmin>> setLogin();
}
