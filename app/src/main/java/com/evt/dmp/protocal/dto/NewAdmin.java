package com.evt.dmp.protocal.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewAdmin {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("pass")
    @Expose
    String pass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
