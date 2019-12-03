package com.example.myapplication.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupLearning {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("NextSchedule")
    @Expose
    private String nextSchedule;
    @SerializedName("Duration")
    @Expose
    private Integer duration;

    public GroupLearning(String name, String nextSchedule, Integer duration) {
        this.name = name;
        this.nextSchedule = nextSchedule;
        this.duration = duration;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextSchedule() {
        return nextSchedule;
    }

    public void setNextSchedule(String nextSchedule) {
        this.nextSchedule = nextSchedule;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
