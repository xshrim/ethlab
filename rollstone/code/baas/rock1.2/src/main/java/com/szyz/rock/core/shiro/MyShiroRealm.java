package com.szyz.rock.core.shiro;

import com.szyz.rock.model.User;
import com.szyz.rock.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    /**
     * 授权管理
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("===================doGetAuthorizationInfo权限管理");
      /*  if(principalCollection ==null)
            throw new AuthorizationException("principal不能为空");

        SysUser user = (SysUser) principalCollection.fromRealm(getName()).iterator().next();
        String userId = user.getUserId();

        //获取用户响应的permission
        List<String> permissions = userService.getSysUserPermission(userId); // 查询数据库
        for(String per : permissions){
            System.out.println("-------" + per);
        }
*/
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
       /* info.addStringPermissions(permissions);
        if(ConfigKeys.SUPER_MANAGER.equals(userId)){ //判断是超级管理员
            info.addRole(ConfigKeys.ASSUME_SUPER_MANAGER_ROLE_NAME);
        }*/
        return info;
    }

    /**
     * 登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("===================doGetAuthenticationInfo登录验证");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = usernamePasswordToken.getUsername();
        if(StringUtils.isEmpty(username)){
            throw new AccountException("用户名不能为空");
        }

        //限制ip 暂时不用
       /* List<String> ipList = userService.getLimitIpList();
        String curIp = Utils.getRemoteIp(Utils.getRequest());
        boolean allow = false;
        for(String ip : ipList){
            allow = compareIp(ip,curIp);
            if(allow)
                break;
        }
        if(!allow)
            throw new IpLimitException("IP不允许访问");*/

        User user = userService.getUserByUserName(username); //用户名 userName
        if(user == null)
            throw new UnknownAccountException("用户不存在");

        return new SimpleAuthenticationInfo(user,user.getPwd(),getName());
    }

    /*
    验证ip
     */
    private boolean compareIp(String templateIp, String realIp){

        if(templateIp.lastIndexOf("*") == -1){
            return templateIp.equals(realIp);
        }

        String[] templateIpstrs = templateIp.split(".");
        String[] realIpstrs = realIp.split(".");
        for(int x = 0; x < 4; x++){
            String sip = templateIpstrs[x];
            if(sip.lastIndexOf("*") == -1){
                String dip = realIpstrs[x];
                if(!sip.equals(dip)){
                    return false;
                }
            }
        }

        return true;
    }
}
