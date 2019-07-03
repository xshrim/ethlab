package com.szyz.rock.core.shiro;

import com.google.gson.Gson;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建JSON的返回格式
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter
{

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                return this.executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            //this.saveRequestAndRedirectToLogin(request, response);

            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            Map<String,Object> map = new HashMap<>();
            map.put("code",-1000);
            map.put("msg","用户未登录或会话已过期");
            Gson gson = new Gson();


            httpServletResponse.getWriter().write(gson.toJson(map));
            return false;
        }
    }

}
