package com.r.uebook.data.remote.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vivek panchal on 08-09-2020.
 * https://vivekpanchal.dev
 **/
public class ValidationResponse {

    @SerializedName("resp")
    String resp;

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }
}
