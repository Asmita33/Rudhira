package com.example.bloodbuddy;

public class Message
{
    private String msgID, msg, senderID;
    private long timeStamp;

    public Message() {
    }

    public Message(String msg, String senderID, long timeStamp) {
        this.msg = msg;
        this.senderID = senderID;
        this.timeStamp = timeStamp;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
