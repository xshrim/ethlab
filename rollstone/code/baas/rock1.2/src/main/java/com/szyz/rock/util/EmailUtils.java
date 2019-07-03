package com.szyz.rock.util;

import org.apache.commons.mail.SimpleEmail;

import java.util.Date;

public class EmailUtils {

    private static String host = "smtp.yeah.net";
    //int port = 465;
    private static String userName = "maybe_code@yeah.net";
    private static String password = "xiaofan123";
    //String to = "839284942@qq.com";

    public static boolean sendEmail(String to ,String context){
        try{
            SimpleEmail mail = new SimpleEmail();
            // 设置邮箱服务器信息
            //  mail.setSmtpPort(port);
            mail.setHostName(host);
            // 设置密码验证器
            mail.setAuthentication(userName, password);
            // 设置邮件发送者
            mail.setFrom(userName);
            // 设置邮件接收者
            mail.addTo(to);
            // 设置邮件编码
            mail.setCharset("UTF-8");
            // 设置邮件主题
            mail.setSubject("Test Email");
            // 设置邮件内容
            mail.setMsg(context);
            // 设置邮件发送时间
            mail.setSentDate(new Date());
            // 发送邮件
            String send = mail.send();
            System.out.println(send);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

}
