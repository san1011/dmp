package com.evt.evt.dmp.protocal;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by everitime5 on 2018-01-22.
 */

public interface DmpWebService {
    public static final String API_URL = "http://192.168.10.43:1338/dmp/"; //todo 서버컴퓨터 변경

    @POST("plan")
    Call<ArrayList<PlanItem>> setPlan(@Body ArrayList<PlanItem> planItem);

    @GET("plan")
    Call<ArrayList<PlanItem>> getAllPlan();

    @GET("plan/{id}/{date}")
    Call<ArrayList<PlanItem>> getPlan(@Path("id") String id, @Path("date") String date);
}
