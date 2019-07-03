package com.szyz.rock.model;

import com.google.gson.Gson;
import com.szyz.rock.util.DateTimeUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Perm {

    private String id; //权限id
    private String tid = ""; // 权限父id
    private String sgerId; //授权者（用户名）
    private String userId; // 被授权者（用户名）
    private String itemId; // 资源id
    private String phash = "";
    private String device =""; //设备
    private String pmark = ""; //水印
    private List<BigInteger> prvs; //权限
/*
    int64[4] prvs;        // 授权权限([是否可查看, 是否可下载源文件, 是否可下载加密文件, 是否可向下授权])
        int64[2] ptime;        // 授权查看时间([总时间, 剩余时间])
        int64[2] ptimes;       // 授权查看次数([总次数, 剩余次数])
        int64[2] pslice;        // 授权查看时间段([起始时间, 结束时间])
        int64[2] ptimestamp;   // 授权查看时间戳([生成时间戳, 过期时间戳])
        int64 ptype;           // 授权类型(0: 独立授权, 1: 委托授权)
    int64 status;          // 授权权限状态(0: 不存在, 1: 正常, 2：冻结， 6: 不可用)
*/
    private List<BigInteger> ptimestamp;
    private List<BigInteger> ptime;
    private List<BigInteger> ptimes;
    private List<BigInteger> pslice;
    private Integer ptype;
    private Integer status;
    private Integer result;

    public String buildDetail(String action,String node,String note){
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("action",action);
        jsonMap.put("object","user");
        jsonMap.put("node",node);
        jsonMap.put("key",id);

        Map<String,Object> permMap = new HashMap<>();
        permMap.put("pid",id);
        permMap.put("tid",tid);
        permMap.put("sgerid",sgerId);
        permMap.put("userid",userId);
        permMap.put("itemid",itemId);
        permMap.put("phash","");
        permMap.put("device",device);
        permMap.put("pmark",pmark);
        permMap.put("prvs",prvs);
        permMap.put("ptime",ptime);
        permMap.put("ptimes",ptimes);
        permMap.put("pslice",pslice);
        permMap.put("ptimestamp",ptimestamp);
        permMap.put("ptype",ptype);
        permMap.put("status",status);
        jsonMap.put("new",permMap);

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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSgerId() {
        return sgerId;
    }

    public void setSgerId(String sgerId) {
        this.sgerId = sgerId;
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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }


    public List<BigInteger> getPtimestamp() {
        return ptimestamp;
    }

    public void setPtimestamp(List<BigInteger> ptimestamp) {
        this.ptimestamp = ptimestamp;
    }

    public List<BigInteger> getPtime() {
        return ptime;
    }

    public void setPtime(List<BigInteger> ptime) {
        this.ptime = ptime;
    }

    public List<BigInteger> getPtimes() {
        return ptimes;
    }

    public void setPtimes(List<BigInteger> ptimes) {
        this.ptimes = ptimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhash() {
        return phash;
    }

    public void setPhash(String phash) {
        this.phash = phash;
    }

    public String getPmark() {
        return pmark;
    }

    public void setPmark(String pmark) {
        this.pmark = pmark;
    }

    public List<BigInteger> getPrvs() {
        return prvs;
    }

    public void setPrvs(List<BigInteger> prvs) {
        this.prvs = prvs;
    }

    public List<BigInteger> getPslice() {
        return pslice;
    }

    public void setPslice(List<BigInteger> pslice) {
        this.pslice = pslice;
    }

    public Integer getPtype() {
        return ptype;
    }

    public void setPtype(Integer ptype) {
        this.ptype = ptype;
    }

    @Override
    public String toString() {
        return "Perm{" +
                "id='" + id + '\'' +
                ", tid='" + tid + '\'' +
                ", sgerId='" + sgerId + '\'' +
                ", userId='" + userId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", phash='" + phash + '\'' +
                ", device='" + device + '\'' +
                ", pmark='" + pmark + '\'' +
                ", prvs=" + prvs +
                ", ptimestamp=" + ptimestamp +
                ", ptime=" + ptime +
                ", ptimes=" + ptimes +
                ", pslice=" + pslice +
                ", ptype=" + ptype +
                ", status=" + status +
                '}';
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
