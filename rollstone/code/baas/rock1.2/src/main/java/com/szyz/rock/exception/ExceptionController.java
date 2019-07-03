package com.szyz.rock.exception;

import com.google.gson.Gson;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ExceptionController // implements ErrorController
{

 /*   @RequestMapping("/error")
    public String handleError(HttpServletResponse response,Exception exception){
        System.out.println("__________/error");
        Map<String,Object> map = new HashMap<>();
        if(exception instanceof NullPointerException){
            map.put("code",-2001);
            map.put("msg","空指针异常");
        }else if (exception instanceof FileNotFoundException){
            map.put("code",-2000);
            map.put("msg","资源404");
        }else if(exception instanceof NumberFormatException){
            map.put("code",-2002);
            map.put("msg","数字转换异常");
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Gson gson = new Gson();

        try{
            response.getWriter().write(gson.toJson(map));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getErrorPath() {
        return null;
    }*/
}
