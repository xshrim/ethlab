package com.szyz.rock.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "item")
public class ItemInfo {

    @Field("iid")
    private String iid;  // 资源id
    @Field("tid")
    private String tid;  // 资源父id
    @Field("uperId")
    private String uperId; // 资源上传者id
    @Field("userId")
    private String userId; // 资源所有着id
    @Field("xhash")
    private String xhash;  //资源文件hash
    @Field("shash")
    private String shash;  //资源加密文件hash
    @Field("ihash")
    private String ihash;  //资源额外信息hash
    @Field("cipher")
    private String cipher; // 资源加密方式
    @Field("ikey")
    private String ikey; //加密key值
    @Field("iopen")
    private Integer iopen; // 资源公开度(0: 不公开, 1: 公司内高级别用户公开, 2: 公司内公开, 4: 平台内高级别用户公开, 5: 平台公开)
    @Field("level")
    private Integer level; // 安全级别 0
    @Field("timestamp")
    private Long timestamp; // 生成时间戳
    @Field("status")
    private Integer status; //资源状态(0: 不存在, 1: 正常, 2：冻结， 6: 不可用)
    @Field("mid")
    private Double mid; //分页id

    @Field("title")
    private String title;
    @Field("content")
    private String content;
    @Field("type")
    private Integer type;
    @Field("createTime")
    private String createTime;
    @Field("path")
    private String path;


    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUperId() {
        return uperId;
    }

    public void setUperId(String uperId) {
        this.uperId = uperId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getXhash() {
        return xhash;
    }

    public void setXhash(String xhash) {
        this.xhash = xhash;
    }

    public String getShash() {
        return shash;
    }

    public void setShash(String shash) {
        this.shash = shash;
    }

    public String getIhash() {
        return ihash;
    }

    public void setIhash(String ihash) {
        this.ihash = ihash;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public Integer getIopen() {
        return iopen;
    }

    public void setIopen(Integer iopen) {
        this.iopen = iopen;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Double getMid() {
        return mid;
    }

    public void setMid(Double mid) {
        this.mid = mid;
    }
}
