package com.szyz.rock.exception;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController1 {

    @ExceptionHandler(value = FileNotFoundException.class)
    public Map<String,Object> e1(HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Gson gson = new Gson();
        map.put("code",-2000);
        map.put("msg","404咯");
        try{
            response.getWriter().write(gson.toJson(map));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
