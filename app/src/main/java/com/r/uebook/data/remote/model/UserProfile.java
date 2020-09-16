package com.r.uebook.data.remote.model;//

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile implements Parcelable {

    @Expose
    @SerializedName("lname")
    private String lastName;
    @Expose
    @SerializedName("fname")
    private String firstName;
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
    public Object userBio;

    @Expose
    @SerializedName("last_seen")
    public Object lastSeen;

    @Expose
    @SerializedName("profile_pic")
    public Object profilePic;

    @Expose
    @SerializedName("user_faceimg_path")
    public Object userFaceimgPath;

    public UserProfile(String user_name, String user_mail, String user_contact_no, Object userBio, String firstName, Object lastSeen, Object profilePic, String lastName, Object userFaceimgPath) {
        this.userName = user_name;
        this.userMail = user_mail;
        this.userContactNo = user_contact_no;
        this.userBio = userBio;
        this.firstName = firstName;
        this.lastSeen = lastSeen;
        this.profilePic = profilePic;
        this.lastName = lastName;
        this.userFaceimgPath = userFaceimgPath;
    }

    public UserProfile(String firstName, String lastName, String user_contact_no, String user_mail) {
        this.userName = firstName + lastName;
        this.userMail = user_mail;
        this.userContactNo = user_contact_no;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    protected UserProfile(Parcel in) {
        lastName = in.readString();
        firstName = in.readString();
        userContactNo = in.readString();
        userMail = in.readString();
        userName = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public Object getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Object lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Object getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Object profilePic) {
        this.profilePic = profilePic;
    }

    public Object getUserFaceimgPath() {
        return userFaceimgPath;
    }

    public void setUserFaceimgPath(Object userFaceimgPath) {
        this.userFaceimgPath = userFaceimgPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lastName);
        dest.writeString(firstName);
        dest.writeString(userContactNo);
        dest.writeString(userMail);
        dest.writeString(userName);
    }
}


