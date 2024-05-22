package com.example.shape.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    //本季度开始时间
    public  Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime())+" 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    //今天是本季度的第几天
    public  int getCurrentQuarterStartTimeday() {
        long days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
//        long time = sdf.parse("2021-01-19").getTime();

        long time = date.getTime();
        long time1 = getCurrentQuarterStartTime()
                .getTime();

        days = (int) ((time -time1) /(24
                * 60 * 60 * 1000));
        return (int) days;
    }


}
