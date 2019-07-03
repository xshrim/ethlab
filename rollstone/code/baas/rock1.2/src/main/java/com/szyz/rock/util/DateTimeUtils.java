package com.szyz.rock.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date str2Date(String  date ,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.isBlank(format)?"yyyy-MM-dd HH:mm:ss":format);

        try{
            return sdf.parse(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String date2Str(Date date , String format){
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.isBlank(format)?"yyyy-MM-dd HH:mm:ss":format);
        try{
            return sdf.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param timestamp 毫秒数
     * @return 秒数 数组
     */
    public static Long[] getOneDayStartAndEndTime(long timestamp){
        Long[] dates = new Long[2];
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        dates[0] = c.getTime().getTime()/1000;
        c.clear();
        c.setTimeInMillis(timestamp);
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        dates[1] = c.getTime().getTime()/1000;
        return dates;
    }

}
