package com.szyz.rock.service;

import com.szyz.rock.model.User;
import com.szyz.rock.model.VaildCode;

import java.util.List;

public interface UserService {
    User getUserByUserName(String userId);
    User getUserInfo(User user,int type);
    int saveUser(User user);
    int authUpdateUser(User user);

    String getVaildCode(String v);
    int saveVaildCode(VaildCode vaildCode);

    List<User> getUserInfoAuthList(User user);
    List<User> selectAllUser();
    int auditingUser(byte status,Long userId);

    int modifyUserInfoSelective(User user,Integer type);

    User getUserTempInfoById(Integer id);

}
