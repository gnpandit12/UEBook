package com.r.uebook.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("resp")
    String resp;


//    public Result(String resp, UserProfile user) {
//        this.resp = resp;
//        this.user = user;
//    }

    public String getResp() {
        return resp;
    }

//    //public UserProfile getUser() {
//        return user;
//    }


}
