package com.szyz.rock.service.impl;

import com.szyz.rock.handle.base.EthAccountHandle;
import com.szyz.rock.mapper.CompanyInfoMapper;
import com.szyz.rock.mapper.UserMapper;
import com.szyz.rock.mapper.VaildCodeMapper;
import com.szyz.rock.model.CompanyInfo;
import com.szyz.rock.model.User;
import com.szyz.rock.model.VaildCode;
import com.szyz.rock.service.UserService;
import com.szyz.rock.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VaildCodeMapper vaildCodeMapper;
    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    @Override
    public User getUserByUserName(String userId) {
        return userMapper.selectUserByUserName(userId);
    }

    @Override
    public User getUserInfo(User user,int type) {
        return userMapper.selectUserInfo(user ,type);
    }

    @Override
    public int saveUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public int authUpdateUser(User user) {
      //  User user1 = userMapper.selectUserByUserName(user.getUserId());
        User user1 = userMapper.selectUserInfo(user, 0);
        if(user1 == null)
            return -1;
        if(user1.getStatus()!=0 && user1.getStatus()!=3)
            return -2;
        User userTemp = userMapper.selectUserTempInfoByUserId(user.getUserId().intValue());
        user.setUserName(user1.getUserName());
        //新认证公司
        if(user.getCompanyId() ==null || user.getCompanyId()==0){
            if(user.getLevel() ==2 || user.getLevel() ==3){
                //这里只适合一个公司管理员的情况
                user.getCompanyInfo().setStatus((byte)0);
                companyInfoMapper.insertCompanyInfo(user.getCompanyInfo());
                user.setCompanyId(user.getCompanyInfo().getId());
            }
        }


        user.setStatus((byte)0); // 0认证 1修改
        userMapper.updateUserStatus((byte)1,user.getUserId().intValue());
        if(userTemp ==null)

            return userMapper.insertUserTempInfo(user); //存入临时用户表
        else {
            return userMapper.updateUserTempInfo(user);
        }
       // return userMapper.updateUserInfo(user);
    }

    @Override
    public String getVaildCode(String v) {
        return vaildCodeMapper.selectVaildCode(v);
    }

    @Override
    public int saveVaildCode(VaildCode vaildCode) {
        return vaildCodeMapper.insertVaildCode(vaildCode);
    }

    @Override
    public List<User> getUserInfoAuthList(User user) {

        /*List<User> users = userMapper.selectUserInfoAuth(user);
        for(User u : users){
            if(u.getCompanyId()==null || u.getCompanyId() ==0)
                continue;
            u.setCompanyInfo(companyInfoMapper.selectCompanyInfoById(u.getCompanyId()));
        }*/
        List<User> users = userMapper.selectUserInfoAuth(user);
        for(User u : users){
            if(u.getStatus() ==0){ //0认证
                if(u.getCompanyId()==null || u.getCompanyId() ==0)
                    continue;
                u.setCompanyInfo(companyInfoMapper.selectCompanyInfoById(u.getCompanyId()));
            }else{ // 1修改
                if(u.getCompanyId()==null || u.getCompanyId() ==0)
                    continue;
                CompanyInfo companyInfo = new CompanyInfo();
                companyInfo.setId(u.getCompanyId());
                companyInfo.setName(u.getCompanyName());
                companyInfo.setApt(u.getCompanyApt());
                u.setCompanyInfo(companyInfo);
            }
        }
        return users;
    }

    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    //@Override
    public int auditingUser(byte status, Long userId) {

//        User user = userMapper.selectUserByUserId(userId.intValue());
        User user = userMapper.selectUserTempInfoByUserId(userId.intValue());
        if(user ==null)
            return - 2;

        if(status == 2){ // 通过
            EthAccountHandle ethAccountHandle = new EthAccountHandle();
            if(user.getStatus() == 0){ // 认证
                boolean flag = ethAccountHandle.addUser(user);
                if(flag){
                   /* User userDao = new User();
                    userDao.setUserId(userId);
                    userDao.setEthAddr(user.getEthAddr());
                    userDao.setStatus(status);
                    userDao.setHash(user.getHash());
                    userMapper.updateUserInfoSelective(userDao);*/
                    user.setStatus((byte)2);
                    userMapper.updateUserInfoSelective(user);
                    userMapper.deleteUserTempInfoById(user.getUserId().intValue());
                    if(user.getCompanyId()!=null && user.getCompanyId() !=0)
                        return companyInfoMapper.updateCompanyStatus((byte)1,user.getCompanyId());
                    //int res = userMapper.updateUserStatus(status,userId);
                }else
                    return -3;
            }else if(user.getStatus() ==1){ // 修改
                //modifyUserInfoSelective(user);
                String res = ethAccountHandle.updateUserHash(user);
                System.out.println(res);
                if("0x1".equals(res)) {
                    if(user.getCompanyId()!=null && user.getCompanyId() !=0) {
                        CompanyInfo companyInfo = new CompanyInfo();
                        companyInfo.setId(user.getCompanyId());
                        companyInfo.setName(user.getCompanyName());
                        companyInfo.setApt(user.getCompanyApt());
                        companyInfoMapper.updateCompanyInfoById(companyInfo);
                    }
                    userMapper.deleteUserTempInfoById(user.getUserId().intValue());
                    user.setStatus((byte)2);
                    return userMapper.updateUserInfoSelective(user);
                }else
                    return  -3;
            }
            return userMapper.updateUserStatus(status,userId.intValue());
        }else{
            if(user.getStatus() ==0){
                userMapper.updateUserTempInfoStatusById(3,userId.intValue());
                return userMapper.updateUserStatus(status,userId.intValue());
            }else{
                return userMapper.updateUserTempInfoStatusById(3,userId.intValue());
            }
        }
        //return 0;
    }

    @Override
    public int modifyUserInfoSelective(User user,Integer type) {
        //FIXME 修改链上数据

        if(type ==1){
            user.setBirthday(DateTimeUtils.str2Date(user.getBirthdayStr(),"yyyy-MM-dd"));
            EthAccountHandle ethAccountHandle = new EthAccountHandle();
            String res = ethAccountHandle.updateUserHash(user);
            System.out.println(res);
            if("0x1".equals(res))
                //return userMapper.updateUserInfoSelective(user);
               // return userMapper.updateUserInfo(user);
                return userMapper.updateUserInfoNoAuth(user);

        }else if(type ==2) {

            User userTemp = userMapper.selectUserTempInfoByUserId(user.getUserId().intValue());
            User user1 = userMapper.selectUserByUserId(user.getUserId().intValue());
            user.setPhone(user1.getPhone());
            user.setEmail(user1.getEmail());
            user.setAddr(user1.getAddr());
            user.setBirthday(user1.getBirthday());
            user.setSex(user1.getSex());
            user.setStatus((byte)1);
            if(userTemp == null){
                return userMapper.insertUserTempInfo(user);
            }else{
                return userMapper.updateUserTempInfo(user);
            }
        }
        //user.updateDetail("update",)

        return 0;
    }

    @Override
    public User getUserTempInfoById(Integer id) {
        return userMapper.selectUserTempInfoByUserId(id);
    }
}
