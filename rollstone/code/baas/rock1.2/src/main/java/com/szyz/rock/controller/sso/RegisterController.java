package com.szyz.rock.controller.sso;

import com.szyz.rock.model.User;
import com.szyz.rock.model.VaildCode;
import com.szyz.rock.service.UserService;
import com.szyz.rock.util.EmailUtils;
import com.szyz.rock.util.MD5;
import com.szyz.rock.util.Utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("**.action")
public class RegisterController {

    @Autowired
    private UserService userService;

    /**
     * 注册用户
     * @param user
     * @param vaildCode 验证码
     * @return code: 0成功 -1失败 msg：code为-1时返回
     */
    @RequestMapping("user/register")
    public Map<String,Object> register(User user,String vaildCode){
        Map<String,Object> resultMap = new HashMap<>();

        if(Utils.isBlank(user.getUserName()) || Utils.isBlank(user.getPwd()) || (Utils.isBlank(user.getEmail()) && Utils.isBlank(user.getPhone()))){
            resultMap.put("code",-1);
            resultMap.put("msg","参数不能为空");
            return resultMap;
        }

        //FIXME 记得打开
       /* if(Utils.isNotBlank(user.getEmail())){
            String code = userService.getVaildCode(user.getEmail());
            if(Utils.isBlank(code) || !code.equals(vaildCode)){
                resultMap.put("code",-1);
                resultMap.put("msg","验证码不正确");
                return resultMap;
            }
        }*/

        User userInfo = userService.getUserInfo(user, 1);
        if(userInfo!=null){
            resultMap.put("code",-1);
            if(userInfo.getUserId().equals(user.getUserId()))
                resultMap.put("msg","用户名已存在");
            if(userInfo.getPhone() !=null && userInfo.getPhone().equals(user.getPhone()))
                resultMap.put("msg","手机号码已存在");
            if(userInfo.getEmail()!= null && userInfo.getEmail().equals(user.getEmail()))
                resultMap.put("msg","电子邮箱已存在");

            return resultMap;
        }
        user.setPwd(MD5.crypt(user.getPwd()));
        int res = userService.saveUser(user);
        if(res>0){
            resultMap.put("code",0);
        }else{
            resultMap.put("code",-1);
            resultMap.put("msg","注册失败");
        }
        return resultMap;
    }


    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @return code： 0成功 -1失败
     */
    @RequestMapping("user/checkEmail")
    public Map<String,Object> checkEmail(String email){
        Map<String,Object> resultMap = new HashMap<>();

        if (Utils.isBlank(email)) {
            resultMap.put("code",-1);
            resultMap.put("msg","邮箱不能为空");
            return resultMap;
        }

        String code = RandomStringUtils.randomAlphabetic(6);
        boolean b = EmailUtils.sendEmail(email, "您的验证码：" + code);
        if(b){
            VaildCode vaildCode = new VaildCode();
            vaildCode.setSms(email);
            vaildCode.setCode(code);
            userService.saveVaildCode(vaildCode);
        }

        resultMap.put("code",b==true?0:-1);
        return resultMap;
    }
}
