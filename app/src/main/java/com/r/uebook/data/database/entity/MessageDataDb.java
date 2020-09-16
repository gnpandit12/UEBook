package com.r.uebook.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.r.uebook.data.database.converter.TimeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "MessageData")
public class MessageDataDb {

    @PrimaryKey(autoGenerate = false)
    @Expose
    @NonNull
    @SerializedName("msgID")
    private String msgId;
    @Expose
    @SerializedName("msg")
    private String msg;
    @Expose
    @SerializedName("room")
    private String room;

    @Expose
    @SerializedName("isSelected")
    private boolean isSelected;

    @TypeConverters(TimeConverters.class)
    @Expose
    @ColumnInfo(name = "creation_date")
    @SerializedName("timeStamp")
    private Date timestamp = new Date(System.currentTimeMillis());

    @Expose
    @SerializedName("rcvr")
    private String receiver;
    @Expose
    @SerializedName("sender")
    private String sender;
    @Expose
    @SerializedName("resp")
    private String resp;

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public MessageDataDb(@NotNull String msgId, String msg, String room,
                         String receiver, String sender, String resp) {
        this.msgId = msgId;
        this.msg = msg;
        this.room = room;
        this.receiver = receiver;
        this.sender = sender;
        this.resp = resp;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    @NotNull
    @Override
    public String toString() {
        return "MessageDataDb{" +
                "msgId='" + msgId + '\'' +
                ", msg='" + msg + '\'' +
                ", room='" + room + '\'' +
                ", timestamp=" + timestamp +
                ", receiver='" + receiver + '\'' +
                ", sender='" + sender + '\'' +
                ", resp='" + resp + '\'' +
                '}';
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
