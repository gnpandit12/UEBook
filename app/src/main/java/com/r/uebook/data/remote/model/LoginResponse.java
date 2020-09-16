package com.r.uebook.data.remote.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse implements Parcelable {

    @SerializedName("resp")
	String resp;

    @SerializedName("count")
    int count;

    @SerializedName("friends")
    public List<Friend> friends;

    @SerializedName("userProfile")
    public List<UserProfile> userProfile;


    protected LoginResponse(Parcel in) {
        resp = in.readString();
        count = in.readInt();
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

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

	public void setResp(String resp) {
		this.resp = resp;
	}


	public String getResp() {
	    return resp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resp);
        dest.writeInt(count);
    }
}