package com.r.uebook.data.remote.model.socket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * Created by vivek panchal on 10-09-2020.
 * https://vivekpanchal.dev
 **/
public class DeleteEventResponse {


    @Expose
    @SerializedName("msgid")
    private String msgid;
    @Expose
    @SerializedName("sender")
    private String sender;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @NotNull
    @Override
    public String toString() {
        return "DeleteEventResponse{" +
                "msgid='" + msgid + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
