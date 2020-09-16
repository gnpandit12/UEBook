package com.r.uebook.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Root{

    @SerializedName("resp")
    String resp;

    @SerializedName("count")
    int count;

    @SerializedName("friends")
    public List<Friend> friends;

    @SerializedName("userProfile")
    public List<UserProfile> userProfile;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(List<UserProfile> userProfile) {
        this.userProfile = userProfile;
    }

    public Root(String resp) {
        this.resp = resp;
    }
    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }




}
