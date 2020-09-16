package com.r.uebook.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Friend implements Parcelable {
    @Expose
    @SerializedName("lname")
    private String lname;
    @Expose
    @SerializedName("fname")
    private String fname;
    @Expose
    @SerializedName("user_contact_no")
    private String userContactNo;
    @Expose
    @SerializedName("user_mail")
    private String userMail;
    @Expose
    @SerializedName("user_name")
    private String userName;

    @Expose
    @SerializedName("user_bio")
    private Object userBio;
    @Expose
    @SerializedName("last_seen")
    private String lastSeen;
    @Expose
    @SerializedName("profile_pic")
    private String profilePic;

    @NotNull
    @Override
    public String toString() {
        return "Friend{" +
                "lname='" + lname + '\'' +
                ", fname='" + fname + '\'' +
                ", userContactNo='" + userContactNo + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userName='" + userName + '\'' +
                ", userBio=" + userBio +
                ", lastSeen='" + lastSeen + '\'' +
                ", profilePic=" + profilePic +
                '}';
    }

    public Friend(String user_name, String user_mail, String user_contact_no, Object user_bio,
                  String fname, String lastSeen, String profilePic, String lname) {
        this.userName = user_name;
        this.userMail = user_mail;
        this.userContactNo = user_contact_no;
        this.userBio = user_bio;
        this.fname = fname;
        this.lastSeen = lastSeen;
        this.profilePic = profilePic;
        this.lname = lname;
    }

    protected Friend(Parcel in) {
        lname = in.readString();
        fname = in.readString();
        userContactNo = in.readString();
        userMail = in.readString();
        userName = in.readString();
        lastSeen = in.readString();
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUserContactNo() {
        return userContactNo;
    }

    public void setUserContactNo(String userContactNo) {
        this.userContactNo = userContactNo;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getUserBio() {
        return userBio;
    }

    public void setUserBio(Object userBio) {
        this.userBio = userBio;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lname);
        dest.writeString(fname);
        dest.writeString(userContactNo);
        dest.writeString(userMail);
        dest.writeString(userName);
        dest.writeString(lastSeen);
    }
}
