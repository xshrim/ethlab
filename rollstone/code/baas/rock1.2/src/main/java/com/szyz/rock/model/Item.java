package com.szyz.rock.model;

import com.google.gson.Gson;
import com.szyz.rock.model.entity.ItemInfo;
import com.szyz.rock.model.entity.PermInfo;
import com.szyz.rock.util.MD5;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String iid;  // 资源id
    private String tid;  // 资源父id
    private String uperId; // 资源上传者id
    private String userId; // 资源所有着id
    private String xhash;  //资源文件hash
    private String shash;  //资源加密文件hash
    private String ihash;  //资源额外信息hash
    private String cipher; // 资源加密方式
    private String ikey; //加密key值
    private Integer iopen; // 资源公开度(0: 不公开, 1: 公司内高级别用户公开, 2: 公司内公开, 4: 平台内高级别用户公开, 5: 平台公开)
    private Integer level; // 安全级别 0
    private Long timestamp; // 生成时间戳
    private Integer status; //资源状态(0: 不存在, 1: 正常, 2：冻结， 6: 不可用)

    private String title; //名称
    private String content; // 描述
    private Integer type; //类型
    private String createTime; //创建时间
    private String path; // 地址

    private String detail;

    private String permId;
    private PermInfo permInfo;

    public String buildHash(){
        StringBuffer sb = new StringBuffer();
        sb.append(title).append(content).append(type).append(createTime);
        return MD5.crypt(sb.toString());
    }

    public String getPath(){
        return path;
    }
    public void setPath(String path){
        this.path = path;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIopen() {
        return iopen;
    }

    public void setIopen(Integer iopen) {
        this.iopen = iopen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String buildDetail(String action ,String node ,String note){
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("action",action);
        jsonMap.put("object","item");
        jsonMap.put("node",node);
        jsonMap.put("key",iid);
        jsonMap.put("note",note);

        Map<String,Object> itemMap = new HashMap<>();
        itemMap.put("iid",iid);
        itemMap.put("tid",tid);
        itemMap.put("uperid",uperId);
        itemMap.put("userid",userId);
        itemMap.put("xhash",xhash);
        itemMap.put("shash",shash);
        itemMap.put("ihash",ihash);
        itemMap.put("cipher",cipher);
        itemMap.put("ikey",ikey);
        itemMap.put("iopen",iopen);
        itemMap.put("level",level);
        itemMap.put("status",status);

        jsonMap.put("new",itemMap);
        Map<String,Object> attMap = new HashMap<>();
        attMap.put("title",title);
        attMap.put("content",content);
        attMap.put("type",type);
        jsonMap.put("att",attMap);
        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
    public String updateDetail(String action ,String node ,String note ,Item newItem){
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("action",action);
        jsonMap.put("object","item");
        jsonMap.put("node",node);
        jsonMap.put("key",iid);
        jsonMap.put("note",note);

        /*Map<String,Object> oldMap = new HashMap<>();
        oldMap.put("iid",iid);
        oldMap.put("tid",tid);
        oldMap.put("uperid",uperId);
        oldMap.put("userid",userId);
        oldMap.put("xhash",xhash);
        oldMap.put("shash",shash);
        oldMap.put("ihash",ihash);
        oldMap.put("cipher",cipher);
        oldMap.put("ikey",ikey);
        oldMap.put("iopen",iopen);
        oldMap.put("level",level);
        oldMap.put("status",status);

        jsonMap.put("new",oldMap);*/
        Map<String,Object> attMap = new HashMap<>();
        if(!title.equals(newItem.getTitle()))
            attMap.put("title",newItem.getTitle());
        if(!content.equals(newItem.getContent()))
            attMap.put("content",newItem.getContent());
        if(type != newItem.getType())
            attMap.put("type",newItem.getType());


        jsonMap.put("att",attMap);

        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public PermInfo getPermInfo() {
        return permInfo;
    }

    public void setPermInfo(PermInfo permInfo) {
        this.permInfo = permInfo;
    }

    @Override
    public String toString() {
        return "Item{" +
                "iid='" + iid + '\'' +
                ", tid='" + tid + '\'' +
                ", uperId='" + uperId + '\'' +
                ", userId='" + userId + '\'' +
                ", xhash='" + xhash + '\'' +
                ", shash='" + shash + '\'' +
                ", ihash='" + ihash + '\'' +
                ", cipher='" + cipher + '\'' +
                ", ikey='" + ikey + '\'' +
                ", iopen=" + iopen +
                ", level=" + level +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", createTime='" + createTime + '\'' +
                ", path='" + path + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
