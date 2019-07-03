package com.szyz.rock.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Document(collection = "log")
public class LogInfo {

    /*@Field("lid")
    private String lid;
    @Field("userid")
    private String userId;

    private String itemId;
    private String permId;
    private*/

   /* private String sn;
    private String detail;
    private String duration;
    private String itemId ;
    private String operate;
    private String permId;
    private String sender;
    private String senderId;
    private String userId;*/

    @Field("sn")
    private String sn;
    @Field("details")
    private Map<String,Object> details;
    @Field("duration")
    private Long duration;
    @Field("itemid")
    private String itemId;
    @Field("mid")
    private Double mid;
    @Field("operate")
    private String operate;
    @Field("senderid")
    private String sender;
    @Field("timestamp")
    private Long timestamp;
    @Field("userid")
    private String userId;

    private Long timeStart;
    private Long timeEnd;


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Double getMid() {
        return mid;
    }

    public void setMid(Double mid) {
        this.mid = mid;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }
}
