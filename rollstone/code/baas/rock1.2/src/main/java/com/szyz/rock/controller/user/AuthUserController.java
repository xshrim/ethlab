package com.szyz.rock.controller.user;

import com.szyz.rock.model.User;
import com.szyz.rock.service.CompanyService;
import com.szyz.rock.service.UserService;
import com.szyz.rock.util.DateTimeUtils;
import com.szyz.rock.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("**.action")
public class AuthUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;

    /**
     * 提交认证信息
     * @param user 用户实体类
     * @return code:0提交成功 -1提交失败 msg：code为-1时返回
     */
    @RequestMapping(value = "auth/submitInfo")
    public Map<String,Object> authSumit(User user){
        Map<String,Object> resultMap = new HashMap<>();

        //-------------假数据
        /*user.setUserId("zlg");
        user.setPhone("15084450212");
        user.setEmail("zlg@qq.com");
        user.setIdcard("22199922992999999");
        user.setIdcardPhoto("addrpath");
        user.setCompanyApt("aptpath");
        user.setCompanyId("111");
        user.setBirthday(new Date());
        user.setLevel((byte)1);
        user.setSex((byte)1);
        user.setStatus((byte)1);
        user.setAddr("北京海淀区");*/
        //-------------

        if(Utils.isNotBlank(user.getBirthdayStr()))
            user.setBirthday(DateTimeUtils.str2Date(user.getBirthdayStr(),"yyyy-MM-dd"));
        int res = userService.authUpdateUser(user);

        if(res ==-1){
            resultMap.put("code",-1);
            resultMap.put("msg","用户不存在");
        }else if(res ==0){
            resultMap.put("code",-1);
            resultMap.put("msg","提交失败");
        }else if(res ==-2){
            resultMap.put("code",-1);
            resultMap.put("msg","已提交认证或已认证");
        }else {
            resultMap.put("code", 0);
            User u = (User)SecurityUtils.getSubject().getPrincipal();
            u.setStatus((byte)1);
        }
        return resultMap;
    }

    /**
     * 认证审核列表
     * @return
     * code:0查询成功 -1 查询失败 msg:code为-1时返回
     * users[
     * 		{
     * 			userId:用户id
     * 			userName:用户名称
     * 			phone:手机号
     * 			email:电子邮箱
     * 			realName:真实姓名
     * 			idcard:身份证号码
     * 			idcardPhoto:身份证照片地址
     * 			companyId:公司id
     * 			addr:地址
     * 			birthdayStr：生日
     * 			sex:性别
     * 			level:用户级别 1：平台管理员；2：企业管理员；3：子公司管理员；4：企业/公司委托管理员；5：企业/公司员工；6：普通认证账户；7：非认证账户
     * 			createTime:注册时间
     * 			companyInfo:{
     * 				id:公司id
     * 				name:公司名称
     * 				apt:公司资质证明
     * 				createTime:提交时间
     * 			}
     * 			status:0认证 1修改
     * 		}
     * 	]
     */
    @RequestMapping(value = "auth/authList")
    public Map<String,Object> authList(){
        Map<String,Object> resultMap = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user ==null){
            resultMap.put("code" ,-1);
            resultMap.put("msg","用户未登录，请重新登录");
        }

        List<User> users = userService.getUserInfoAuthList(user);

        resultMap.put("code",0);
        resultMap.put("users",users);

        return resultMap;
    }

    /**
     * 认证审核
     * @param status 审核状态  2通过 3驳回
     * @param userId 用户ID
     * @return
     */
    @RequestMapping(value = "auth/auditing")
    public Map<String,Object> auditing(byte status,Long userId){
        Map<String,Object> resultMap = new HashMap<>();
        int res = userService.auditingUser(status,userId);
        resultMap.put("code",res==1?0:-1);
        if(res==0)
            resultMap.put("msg","操作失败");
        else if(res ==-3)
            resultMap.put("msg","区块链adduser失败");
        else if(res ==-2)
            resultMap.put("msg","用户不存在");
        return resultMap;
    }

    /**
     * 获取修改用户资料页用户信息
     * @return
     * code:0查询成功
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
     * 		companyInfo:{
     * 			id:公司id
     * 			name:公司名称
     * 			apt:公司资质证明
     * 			createTime:提交时间
     * 		}
     * 	}
     * 	changeStatus：1审核中 3未通过 -1则不显示
     * 	status:1不可以修改 0可以修改
     */
    @RequestMapping(value = "auth/getAuthUserInfo")
    public Map<String,Object> getAuthUserInfo(){
        Map<String,Object> resultMap = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        User userTempInfo = userService.getUserTempInfoById(user.getUserId().intValue());
        resultMap.put("changeStatus",-1);
        if(userTempInfo != null) {
            if(user.getStatus() ==3 || user.getStatus() ==0)
                resultMap.put("changeStatus",-1);
            else
                resultMap.put("changeStatus",(userTempInfo.getStatus() ==1 || userTempInfo.getStatus()==3)?userTempInfo.getStatus():-1);
            if(userTempInfo.getCompanyId()!=null && userTempInfo.getCompanyId()!=0)
                userTempInfo.setCompanyInfo(companyService.getCompanyInfoById(user.getUserId().intValue()));

            resultMap.put("status", userTempInfo.getStatus() !=3?1:0); // 1不可以修改 0可以修改
//            userTempInfo.setStatus(userTempInfo.getStatus()!=3?(byte)1:3);
            userTempInfo.setStatus(user.getStatus());
            resultMap.put("userInfo",userTempInfo);
        }else {
            User userInfo = userService.getUserInfo(user, 0);
            if(userInfo!=null && userInfo.getCompanyId()!=null && userInfo.getCompanyId()!=0){
                userInfo.setCompanyInfo(companyService.getCompanyInfoById(user.getCompanyId().intValue()));
            }
            resultMap.put("userInfo",userInfo);
            resultMap.put("status",0);
        }

        return resultMap;
    }
}
