package com.yaa.trading.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {

    public static double format(Double o){
        if(o == null){
            return 0;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(o));
    }

    public static String format(Long time){
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

}
