package com.yaa.trading.util;



import com.yaa.trading.comm.Aggregation;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Random;

public class DateUtil {



    public static Long adjustTime(Long mills, Aggregation aggregation) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        calendar.set(Calendar.MILLISECOND, 0);

        // 修正时间
        switch (aggregation.getType()) {

            case m:
                calendar.set(Calendar.SECOND, 0);
                int m = calendar.get(Calendar.MINUTE) / aggregation.getValue() * aggregation.getValue();
                calendar.set(Calendar.MINUTE, m);
                return calendar.getTimeInMillis();
            case h:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                int h = calendar.get(Calendar.HOUR_OF_DAY) / aggregation.getValue() * aggregation.getValue();
                calendar.set(Calendar.HOUR_OF_DAY, h);
                return calendar.getTimeInMillis();
            case d:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                return calendar.getTimeInMillis();
        }
        throw new RuntimeException("Unsupported aggregation type:" + aggregation.getType().name());
    }

    public static BigDecimal[] getData() {
        BigDecimal[] data = new BigDecimal[5];
        double high = 0.0;
        double low = 0.0;
        double openprice = 0.0;
        double closeprice = 0.0;
        double volumequantity = 0.0;
        int max = new Random().nextInt(12)+2200;
        int min = 2200 - new Random().nextInt(10);
        int volume = 20 - new Random().nextInt(10);
        double degree = new BigDecimal(new Random().nextFloat()).doubleValue();
        high = max + degree ;
        degree = new BigDecimal(new Random().nextFloat()).doubleValue();
        low = min - degree ;
        if(max-min <= 0){
            return null;
        }
        int open = new Random().nextInt(max - min) + min;
        degree = new BigDecimal(new Random().nextFloat()).doubleValue();
        openprice = open + degree;
        if(openprice>high)
            openprice = high;
        int close = new Random().nextInt(max - min) + min;
        degree = new BigDecimal(new Random().nextFloat()).doubleValue();
        closeprice = close + degree ;
        if(closeprice<low)
            closeprice = low;

        degree = new BigDecimal(new Random().nextFloat()).doubleValue();
        volumequantity = volume + degree ;

        data[0] = new BigDecimal(high).setScale(2,BigDecimal.ROUND_DOWN);
        data[1] = new BigDecimal(low).setScale(2,BigDecimal.ROUND_DOWN);
        data[2] = new BigDecimal(openprice).setScale(2,BigDecimal.ROUND_DOWN);
        data[3] = new BigDecimal(closeprice).setScale(2,BigDecimal.ROUND_DOWN);
        data[4] = new BigDecimal(volumequantity).setScale(4,BigDecimal.ROUND_DOWN);
        return data ;
    }


    public static void main(String args[]){
        double random = Math.ceil((Math.random()*10+1)*100)/100;
        System.out.println(random);

    }

}
