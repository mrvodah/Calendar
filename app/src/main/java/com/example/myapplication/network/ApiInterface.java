package com.example.myapplication.network;

import com.example.myapplication.network.models.LiveStreamResponse;
import com.example.myapplication.network.models.ScheduleRequest;
import com.example.myapplication.network.models.ScheduleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("dashboard/teacher")
    Call<LiveStreamResponse> getLiveStreams(@Query("token") String token, @Query("email") String email,
                                            @Query("year") String year);

    @GET("checkSchedulePlan")
    Call<ScheduleResponse> getSchedules(@Query("token") String token, @Query("email") String email);

    @Headers("Content-Type: application/json")
    @POST("savePlanScheduleTeacher")
    Call<LiveStreamResponse> postSchedules(@Body ScheduleRequest request);
}
