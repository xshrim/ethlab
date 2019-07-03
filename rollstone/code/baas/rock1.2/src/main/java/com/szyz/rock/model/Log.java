package com.szyz.rock.model;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Log {

    private String id;  // id就是流水号sn
    private String userId; //用户名
    private String itemId; // 资源id
    private String permId = ""; // 权限id
    private String action; // 动作、行为
    private String desc; // 描述
    private BigInteger duration; // 周期、时长

    public String buildDetails(String node,String note){
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("action",action);
        jsonMap.put("object","log");
        jsonMap.put("node",node);
        jsonMap.put("key",id);

        Map<String,Object> logMap = new HashMap<>();
        logMap.put("userid",userId);
        logMap.put("itemid",itemId);
        logMap.put("permid",permId);
        logMap.put("action",action);
        logMap.put("desc",desc);
        logMap.put("duration",duration);

        jsonMap.put("new",logMap);

        Map<String,Object> attMap = new HashMap<>();
        jsonMap.put("att",attMap);
        jsonMap.put("note",note);
        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigInteger getDuration() {
        return duration;
    }

    public void setDuration(BigInteger duration) {
        this.duration = duration;
    }
}
