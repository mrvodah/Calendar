package com.example.myapplication.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleResponse {

    @SerializedName("schedules")
    @Expose
    private List<Schedule> schedules = null;
    @SerializedName("scheduleHistory")
    @Expose
    private ScheduleHistory scheduleHistory;
    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public ScheduleHistory getScheduleHistory() {
        return scheduleHistory;
    }

    public void setScheduleHistory(ScheduleHistory scheduleHistory) {
        this.scheduleHistory = scheduleHistory;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
