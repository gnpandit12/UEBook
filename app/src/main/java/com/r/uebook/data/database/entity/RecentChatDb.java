package com.r.uebook.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.r.uebook.data.database.converter.FriendConverter;
import com.r.uebook.data.database.converter.TimeConverters;
import com.r.uebook.data.remote.model.Friend;

import java.util.Date;

/**
 * Created by vivek panchal on 11-09-2020.
 * https://vivekpanchal.dev
 **/

@Entity(tableName = "RecentChats")
public class RecentChatDb {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String FriendPhone;

    @ColumnInfo(name = "FriendDetail")
    @TypeConverters(FriendConverter.class)
    private Friend friend;


    @ColumnInfo(name = "lastMsg")
    private String latest_msg;

    @TypeConverters(TimeConverters.class)
    @Expose
    @ColumnInfo(name = "timeStamp")
    private Date timestamp;

    @ColumnInfo(name = "NumOfMsg")
    private int num_msg_unread;

    public RecentChatDb() {
    }

    public RecentChatDb(@NonNull String friendPhone, Friend friend, String latest_msg, Date timestamp, int num_msg_unread) {
        FriendPhone = friendPhone;
        this.friend = friend;
        this.latest_msg = latest_msg;
        this.timestamp = timestamp;
        this.num_msg_unread = num_msg_unread;
    }

    @NonNull
    public String getFriendPhone() {
        return FriendPhone;
    }

    public void setFriendPhone(@NonNull String friendPhone) {
        FriendPhone = friendPhone;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public String getLatest_msg() {
        return latest_msg;
    }

    public void setLatest_msg(String latest_msg) {
        this.latest_msg = latest_msg;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getNum_msg_unread() {
        return num_msg_unread;
    }

    public void setNum_msg_unread(int num_msg_unread) {
        this.num_msg_unread = num_msg_unread;
    }

}
