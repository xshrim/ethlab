package com.szyz.rock.controller.user;

import com.szyz.rock.model.User;
import com.szyz.rock.service.CompanyService;
import com.szyz.rock.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("**.action")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;

    /**
     * 获取用户信息
     * @param user
     * @return
     *  code:0查询成功
     * 	userInfo:{
     * 		userId:用户id
     * 		userName:用户名
     * 		realName:真实姓名
     * 		phone:手机号
     * 		email:电子邮箱
     * 		idcard:身份证号码
     * 		idcardPhoto:证件照
     * 		birthday:生日
     * 		sex:性别 1男 2女
     * 		level:用户级别 1：平台管理员；2：企业管理员；3：子公司管理员；4：企业/公司委托管理员；5：企业/公司员工；6：普通认证账户；7：非认证账户
     * 		status:用户认证状态 0 未认证 1审核中 2通过 3未通过
     * 		createTime:注册时间
     * 	}
     */
    @RequestMapping("user/getUserInfo")
    public Map<String,Object> getUserInfo(User user){ // userId
        Map<String,Object> resultMap = new HashMap<>();
        User userInfo = userService.getUserInfo(user,0);
        resultMap.put("code",0);
        resultMap.put("userInfo",userInfo);
        //System.out.println(userInfo.getDetail("add"));
        return resultMap;
    }

    /**
     * 修改用户资料
     * @param user 用户实体类
     * @param type 1非认证 2认证
     * @return
     *  code:0成功 -1失败
     * 	msg:code为-1时返回
     */
    @RequestMapping("user/modifyUserInfo")
    public Map<String ,Object> modifyUserInfo(User user,Integer type ){

        User u = (User) SecurityUtils.getSubject().getPrincipal();
        user.setUserId(u.getUserId());
        user.setUserName(u.getUserName());
        user.setCreateTime(u.getCreateTime());


        if(type ==1){ //非认证

        }else if(type ==2){ //认证

        }


        int res = userService.modifyUserInfoSelective(user,type);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code","0");
        if(res <=0){
            resultMap.put("code","-1");
            resultMap.put("msg","更新失败");
        }
        return resultMap;
    }

    /**
     * 获取用户列表（暂时）
     * @return
     */
    @RequestMapping("user/getUserList")
    public Map<String,Object> getUserList(){
        Map<String,Object> resultMap = new HashMap<>();
        List<User> users = userService.selectAllUser(); //FIXME 后面改成从MongoDB取
        resultMap.put("code",0);
        resultMap.put("list",users);
        return resultMap;
    }

    /**
     * 获取公司列表（暂时）
     * @return
     */
    @RequestMapping("company/getComapnyList")
    public Map<String,Object> getComapnyList(){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("list",companyService.getCompanyList());
        return resultMap;
    }
}
