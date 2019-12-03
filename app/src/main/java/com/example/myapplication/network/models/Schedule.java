package com.example.myapplication.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("StartTime")
    @Expose
    private Long startTime;
    @SerializedName("EndTime")
    @Expose
    private Long endTime;
    @SerializedName("DayOfWeek")
    @Expose
    private Integer dayOfWeek;
    @SerializedName("WeekOfYear")
    @Expose
    private Integer weekOfYear;
    @SerializedName("StartHour")
    @Expose
    private Long startHour;
    @SerializedName("EndHour")
    @Expose
    private Long endHour;
    @SerializedName("Duration")
    @Expose
    private Long duration;
    @SerializedName("StartTime1991")
    @Expose
    private Integer startTime1991;
    @SerializedName("EndTime1991")
    @Expose
    private Integer endTime1991;
    @SerializedName("Year")
    @Expose
    private Integer year;
    @SerializedName("Month")
    @Expose
    private Integer month;
    @SerializedName("HistoryId")
    @Expose
    private Integer historyId;

    public Schedule(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = "";
        this.startTime1991 = 0;
        this.endTime1991 = 0;
    }

    public Schedule(String description, Long startTime, Long endTime) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public Long getStartHour() {
        return startHour;
    }

    public void setStartHour(Long startHour) {
        this.startHour = startHour;
    }

    public Long getEndHour() {
        return endHour;
    }

    public void setEndHour(Long endHour) {
        this.endHour = endHour;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getStartTime1991() {
        return startTime1991;
    }

    public void setStartTime1991(Integer startTime1991) {
        this.startTime1991 = startTime1991;
    }

    public Integer getEndTime1991() {
        return endTime1991;
    }

    public void setEndTime1991(Integer endTime1991) {
        this.endTime1991 = endTime1991;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
