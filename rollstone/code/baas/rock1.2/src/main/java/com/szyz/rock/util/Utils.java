package com.szyz.rock.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.UUID;

public class Utils {
    public static boolean isBlank(String str){
        return str ==null || "".equals(str) || str.length() ==0;
    }

    public static boolean isNotBlank(String str){

        return str !=null && !"".equals(str) && str.length()>0;
    }

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String getRemoteIp(HttpServletRequest request){
        String ip =  request.getHeader("x-forwarded-for");
        if(!isNotBlank(ip) || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(!isNotBlank(ip) || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(!isNotBlank(ip) || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    public static String getLoginUserProperty(String property) {
        String propertyValue = null;
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        try {
            BeanInfo bi = Introspector.getBeanInfo(principal.getClass());
            PropertyDescriptor[] arr$ = bi.getPropertyDescriptors();
            int len$ = arr$.length;
            for (int i$ = 0; i$ < len$; ++i$) {
                PropertyDescriptor pd = arr$[i$];
              /*  System.out.print("++++++++Security:"+pd.getName() +" ");
                Object value = pd.getReadMethod().invoke(principal,
                        (Object[]) null);
                System.out.println(value);*/
                if (pd.getName().equals(property)) {
                    Object value = pd.getReadMethod().invoke(principal,
                            (Object[]) null);
                    if(value != null)
                        propertyValue = String.valueOf(value);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return propertyValue;
    }

    public static byte[] bytes2bytes(byte[] bytes ,int length){

        byte[] tempBytes = new byte[length];
        for(int i=0 ;i<length ;i ++){
            if(i >= bytes.length)
                break;
            tempBytes[i] = bytes[i];
        }
        return tempBytes;
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
