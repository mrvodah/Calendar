package com.example.myapplication.network.models;

import java.util.List;

public class ScheduleRequest {

    String teacherId;
    List<Schedule> editSchedules;
    List<Schedule> schedules;
    Long startTime;
    Long endTime;

    public ScheduleRequest(String teacherId, List<Schedule> schedules) {
        this.teacherId = teacherId;
        this.schedules = schedules;
    }

    public ScheduleRequest(String teacherId, List<Schedule> schedules, Long startTime, Long endTime) {
        this.teacherId = teacherId;
        this.schedules = schedules;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ScheduleRequest(String teacherId, List<Schedule> editSchedules, List<Schedule> schedules, Long startTime, Long endTime) {
        this.teacherId = teacherId;
        this.editSchedules = editSchedules;
        this.schedules = schedules;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Long getStartDate() {
        return startTime;
    }

    public void setStartDate(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndDate() {
        return endTime;
    }

    public void setEndDate(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ScheduleRequest{" +
                "editSchedules=" + editSchedules +
                ", schedules=" + schedules +
                '}';
    }
}
