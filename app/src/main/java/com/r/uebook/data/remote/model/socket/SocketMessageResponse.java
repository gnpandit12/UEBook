package com.r.uebook.data.remote.model.socket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * Created by vivek panchal on 11-09-2020.
 * https://vivekpanchal.dev
 **/
public class SocketMessageResponse {
    @Expose
    @SerializedName("msgID")
    private String msgId;
    @Expose
    @SerializedName("msg")
    private String msg;
    @Expose
    @SerializedName("room")
    private String room;
    @Expose
    @SerializedName("timeStamp")
    private String timestamp;
    @Expose
    @SerializedName("rcvr")
    private String receiver;
    @Expose
    @SerializedName("sender")
    private String sender;
    @Expose
    @SerializedName("resp")
    private String resp;

    public SocketMessageResponse(String msg, String room, String receiver, String sender, String resp, String msgId) {
        this.msg = msg;
        this.room = room;
        this.receiver = receiver;
        this.sender = sender;
        this.resp = resp;
        this.msgId = msgId;
    }

    public SocketMessageResponse() {
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
        return "SocketMessageResponse{" +
                "msgId='" + msgId + '\'' +
                ", msg='" + msg + '\'' +
                ", room='" + room + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", receiver='" + receiver + '\'' +
                ", sender='" + sender + '\'' +
                ", resp='" + resp + '\'' +
                '}';
    }
}
