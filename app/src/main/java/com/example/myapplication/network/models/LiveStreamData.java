package com.example.myapplication.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveStreamData {

    @SerializedName("passedCourses")
    @Expose
    public Integer passedCourses;
    @SerializedName("activeCourses")
    @Expose
    public Integer activeCourses;
    @SerializedName("incomingVCL")
    @Expose
    public Integer incomingVCL;
    @SerializedName("passedVCL")
    @Expose
    public Integer passedVCL;
    @SerializedName("unreadPublicDiscuss")
    @Expose
    public Integer unreadPublicDiscuss;
    @SerializedName("unreadPrivateDiscuss")
    @Expose
    public Integer unreadPrivateDiscuss;
    @SerializedName("unreviewExercise")
    @Expose
    public Integer unreviewExercise;
    @SerializedName("groupLearnings")
    @Expose
    public List<GroupLearning> groupLearnings = null;
    @SerializedName("todayRooms")
    @Expose
    public List<Object> todayRooms = null;

    public Integer getPassedCourses() {
        return passedCourses;
    }

    public void setPassedCourses(Integer passedCourses) {
        this.passedCourses = passedCourses;
    }

    public Integer getActiveCourses() {
        return activeCourses;
    }

    public void setActiveCourses(Integer activeCourses) {
        this.activeCourses = activeCourses;
    }

    public Integer getIncomingVCL() {
        return incomingVCL;
    }

    public void setIncomingVCL(Integer incomingVCL) {
        this.incomingVCL = incomingVCL;
    }

    public Integer getPassedVCL() {
        return passedVCL;
    }

    public void setPassedVCL(Integer passedVCL) {
        this.passedVCL = passedVCL;
    }

    public Integer getUnreadPublicDiscuss() {
        return unreadPublicDiscuss;
    }

    public void setUnreadPublicDiscuss(Integer unreadPublicDiscuss) {
        this.unreadPublicDiscuss = unreadPublicDiscuss;
    }

    public Integer getUnreadPrivateDiscuss() {
        return unreadPublicDiscuss;
    }

    public void setUnreadPrivateDiscuss(Integer unreadPrivateDiscuss) {
        this.unreadPrivateDiscuss = unreadPrivateDiscuss;
    }

    public Integer getUnreviewExercise() {
        return unreviewExercise;
    }

    public void setUnreviewExercise(Integer unreviewExercise) {
        this.unreviewExercise = unreviewExercise;
    }

    public List<GroupLearning> getGroupLearnings() {
        return groupLearnings;
    }

    public void setGroupLearnings(List<GroupLearning> groupLearnings) {
        this.groupLearnings = groupLearnings;
    }

    public List<Object> getTodayRooms() {
        return todayRooms;
    }

    public void setTodayRooms(List<Object> todayRooms) {
        this.todayRooms = todayRooms;
    }

}
