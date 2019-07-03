package com.szyz.rock.controller.sso;

import com.szyz.rock.model.User;
import com.szyz.rock.service.UserService;
import com.szyz.rock.util.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("**.action")
public class LoginController {

    private static Log LOG =LogFactory.getLog(LoginController.class);

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return
     *  code: 0登录成功 -1登录失败
     * 	msg：code为-1时返回
     * 	userInfo:
     * 	{
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
    @RequestMapping("sso/login")
    public Map<String,Object> login(String username ,String password){

        Map<String,Object> resultMap = new HashMap<>();

        if(Utils.isBlank(username) || Utils.isBlank(password)){
            resultMap.put("code",-1);
            resultMap.put("msg","用户名或密码不能为空");
            return resultMap;
        }

        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        token.setRememberMe(false);

        resultMap.put("code",0);
        Subject subject = SecurityUtils.getSubject();
        String tips = "";
        try{
            subject.login(token);
        }/*catch(IpLimitException ie){
            ie.printStackTrace();
            tips = ie.getMessage();
        }*/catch(UnknownAccountException uae){
            uae.printStackTrace();
            tips = uae.getMessage();
        }catch(IncorrectCredentialsException ice){
            ice.printStackTrace();
            tips = "用户名或密码错误";
        }catch(LockedAccountException lae){
            lae.printStackTrace();
            tips = lae.getMessage();
        }catch(AuthenticationException ae){
            ae.printStackTrace();
            tips = ae.getMessage();
        }
        if(Utils.isNotBlank(tips)){
            resultMap.put("code",-1);
            resultMap.put("msg",tips);
        }
        //model.addAttribute("userId",userId);
//        LOG.info("login success");
        System.out.println("login success");
        User user = new User();
        user.setUserName(username);
        User userInfo = userService.getUserInfo(user, 0);
        resultMap.put("userInfo",userInfo);


        return resultMap;
    }

    /**
     * 退出登录
     * @return code
     */
    @RequestMapping("logout")
    public Map<String,Object> logout(){
        Map<String,Object> resultMap = new HashMap<>();
        SecurityUtils.getSubject().logout();
        resultMap.put("code",0);
        return resultMap;
    }
}
