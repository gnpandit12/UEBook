package com.r.uebook.data.remote.model.socket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vivek panchal on 09-09-2020.
 * https://vivekpanchal.dev
 **/
public  class SocketModel {

    @Expose
    @SerializedName("sID")
    private String sid;
    @Expose
    @SerializedName("mob")
    private String mob;

    public SocketModel( String mob,String sid) {
        this.mob = mob;
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }
}
