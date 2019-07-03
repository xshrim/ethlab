package com.szyz.rock.model.entity;

import com.szyz.rock.model.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.util.List;

@Document(collection = "perm")
public class PermInfo {
//    bytes32 pid;            // 授权权限id
//    bytes32 tid;            // 父权限id
//    bytes32 sgerid;         // 授权者id
//    bytes32 userid;         // 授权用户id
//    bytes32 itemid;         // 授权资源id
//    bytes32 phash;          // 权限额外信息hash
//    bytes32 device;         // 授权查看设备
//    bytes32 pmark;         // 水印内容(0x00: 无水印)
//    int64[4] prvs;        // 授权权限([是否可查看, 是否可下载源文件, 是否可下载加密文件, 是否可向下授权])
//    int64[2] ptime;        // 授权查看时间([总时间, 剩余时间])
//    int64[2] ptimes;       // 授权查看次数([总次数, 剩余次数])
//    int64[2] pslice;        // 授权查看时间段([起始时间, 结束时间])
//    int64[2] ptimestamp;   // 授权查看时间戳([生成时间戳, 过期时间戳])
//    int64 ptype;           // 授权类型(0: 独立授权, 1: 委托授权)
//    int64 status;          // 授权权

    @Id
    private String _id;
    @Field("pid")
    private String pid;
    @Field("tid")
    private String tid;
    @Field("sgerid")
    private String sgerid;
    @Field("userid")
    private String userid;
    @Field("itemid")
    private String itemid;
    @Field("phash")
    private String phash;
    @Field("device")
    private String device;
    @Field("prvs")
    private List<BigInteger> prvs;
    @Field("ptimestamp")
    private List<BigInteger> ptimestamp;
    @Field("ptime")
    private List<BigInteger> ptime;
    @Field("ptimes")
    private List<BigInteger> ptimes;
    @Field("pslice")
    private List<BigInteger> pslice;
    @Field("ptype")
    private Integer ptype;
    @Field("status")
    private Integer status;
    @Field("mid")
    private Double mid;

    private Item item;

    public PermInfo() {
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSgerid() {
        return sgerid;
    }

    public void setSgerid(String sgerid) {
        this.sgerid = sgerid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getPhash() {
        return phash;
    }

    public void setPhash(String phash) {
        this.phash = phash;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public List<BigInteger> getPrvs() {
        return prvs;
    }

    public void setPrvs(List<BigInteger> prvs) {
        this.prvs = prvs;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getMid() {
        return mid;
    }

    public void setMid(Double mid) {
        this.mid = mid;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
