package com.evt.dmp.protocal.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by everitime5 on 2018-01-16.
 */

public class PlanItem implements Serializable {

    @SerializedName("time")
    @Expose
    String time;
    @SerializedName("plan")
    @Expose
    String plan;
    @SerializedName("complete") //1:계획없음 2:성공 -1:실패
    @Expose
    int complete;
    @SerializedName("date")
    @Expose
    String date;
    @SerializedName("id")
    @Expose
    String id;

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getDate() { return date; }

    public void setDate(String date) {this.date = date;}

    public PlanItem() {}

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {this.time = time;}

    public int getComplete() {return complete;}

    public void setComplete(int complete) {this.complete = complete;}

    public PlanItem(String time, String plan, int complete, String date, String id) {
        this.time = time;
        this.plan = plan;
        this.complete = complete;
        this.date = date;
        this.id = id;
    }

    @Override
    public String toString() {
        return "PlanItem{" +
                "time='" + time + '\'' +
                ", plan='" + plan + '\'' +
                ", complete='" + complete + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
