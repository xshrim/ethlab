package com.szyz.rock.model;

import com.google.gson.Gson;
import com.szyz.rock.util.DateTimeUtils;
import com.szyz.rock.util.MD5;
import com.szyz.rock.util.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {

    private Long userId; //用户id（数据库id）
    private String userName; //用户名（区块链用户id）
    private String pwd; //密码（数据库密码）
    private String phone; //手机号
    private String email; // 邮箱地址

    private String realName; // 真实姓名
    private String idcard; //身份证号码
    private String idcardPhoto; //身份证照
    private String companyName;
    private String companyApt;
    private Integer companyId;//公司id
    private String addr;
    private Date birthday;
    private String birthdayStr;
    private byte sex;
    private byte level; //用户级别
    private byte status; //状态
    private Date createTime; //注册时间
    private String ethAddr; //用户区块链地址

    private String hash;
    private String detail;


    public String buildHash(){
        StringBuffer sb = new StringBuffer();
        sb.append(pwd)
                .append(phone)
                .append(email)
                .append(realName)
                .append(idcardPhoto)
                .append(idcard)
                .append(getBirthdayStr())
                .append(ethAddr)
                .append(sex)
                .append(DateTimeUtils.date2Str(createTime,"yyyy-MM-dd HH:mm:ss"));

        return hash = MD5.crypt(sb.toString());
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    private CompanyInfo companyInfo;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcardPhoto() {
        return idcardPhoto;
    }

    public void setIdcardPhoto(String idcardPhoto) {
        this.idcardPhoto = idcardPhoto;
    }

    public String getCompanyApt() {
        return companyApt;
    }

    public void setCompanyApt(String companyApt) {
        this.companyApt = companyApt;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBirthdayStr() {
        if(Utils.isBlank(birthdayStr) && birthday !=null)
            return DateTimeUtils.date2Str(this.birthday,"yyyy-MM-dd");
        return birthdayStr;
    }

    public void setBirthdayStr(String birthdayStr) {
        this.birthdayStr = birthdayStr;
    }

    public String getEthAddr() {
        return ethAddr;
    }

    public void setEthAddr(String ethAddr) {
        this.ethAddr = ethAddr;
    }

    public String updateDetail(String action ,String node,String note ,User newUser){
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("action",action);
        jsonMap.put("object","user");
        jsonMap.put("node",node);
        jsonMap.put("key",userName);
        jsonMap.put("note",note);

        Map<String,Object> oldMap = new HashMap<>();
        Map<String,Object> newMap = new HashMap<>();
        //if(this.ethAddr.equals(user.getEthAddr()))

        if(status != newUser.getStatus()){
            oldMap.put("status",status);
            newMap.put("status",newUser.getStatus());
        }
        if(!buildHash().equals(newUser.buildHash())){
            oldMap.put("uhash",hash);
            newMap.put("uhash",newUser.buildHash());
        }

     /*   oldMap.put("address",ethAddr);
        oldMap.put("uid",userName);
        oldMap.put("eid",companyId);
        oldMap.put("level",level);*/
        Map<String,Object> attMap = new HashMap<>();
        if(!phone.equals(newUser.getPhone()))
            attMap.put("phone",phone);
        if(!email.equals(newUser.getEmail()))
            attMap.put("email",email);
        if(!realName.equals(newUser.getRealName()))
            attMap.put("realName",realName);
        if (!realName.equals(newUser.getIdcard()))
            attMap.put("idcard",idcard);
//        if(birthday !=null && )
//            attMap.put("birthday",DateTimeUtils.date2Str(birthday,"yyyy-MM-dd"));
        if(sex != newUser.getSex())
            attMap.put("sex",sex);
        jsonMap.put("att",attMap);
        jsonMap.put("old",oldMap);
        jsonMap.put("new",newMap);
        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }

    public String buildDetail(String action,String node,String note) {
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("action",action);
        jsonMap.put("object","user");
        jsonMap.put("node",node);
        jsonMap.put("key",userName);

        Map<String,Object> userMap = new HashMap<>();
        userMap.put("address",ethAddr);
        userMap.put("uid",userName);
        userMap.put("eid",companyId ==null||companyId==0?"":companyId.toString());
        userMap.put("uhash",hash);
        userMap.put("level",level);
        userMap.put("status",status);

        jsonMap.put("new",userMap);
        Map<String,Object> attMap = new HashMap<>();
        attMap.put("phone",phone);
        attMap.put("email",email);
        attMap.put("realName",realName);
        attMap.put("idcard",idcard);
        if(birthday !=null)
            attMap.put("birthday",DateTimeUtils.date2Str(birthday,"yyyy-MM-dd"));
        attMap.put("sex",sex);


        jsonMap.put("att",attMap);
        jsonMap.put("note",note);
        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
