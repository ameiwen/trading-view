package com.yaa.trading.server.handler;

import com.yaa.trading.comm.Aggregation;
import com.yaa.trading.comm.AggregationType;
import com.yaa.trading.comm.Const;
import com.yaa.trading.comm.ReqEnum;
import com.yaa.trading.server.bean.ChannelReq;
import com.yaa.trading.server.bean.Kline;
import com.yaa.trading.server.processor.ServerProcessor;
import com.yaa.trading.util.DateUtil;
import com.yaa.trading.util.WsResultUtil;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.math.BigDecimal;
import java.util.*;

public class KlineHisHandler extends ServerProcessor {

    @Override
    public void handleMessage(WsRequest wsRequest, ChannelReq channelReq, ChannelContext channelContext) throws Exception {
        //时间类型
        String resolute = channelReq.getChannel().split("[.]")[1];
        Long time = channelReq.getEndTime()*1000;
        Integer timeSpace = null;
        if (resolute.equals(ReqEnum.ONE_MIN.getName())) {
            timeSpace = Const.ONE_MIN_SPACE;
            time = DateUtil.adjustTime(time, new Aggregation(AggregationType.m, ReqEnum.ONE_MIN.getValue()));
        } else if (resolute.equals(ReqEnum.FIV_MIN.getName())) {
            timeSpace = Const.FIV_MIN_SPACE;
            time = DateUtil.adjustTime(time, new Aggregation(AggregationType.m, ReqEnum.FIV_MIN.getValue()));
        } else if(resolute.equals(ReqEnum.ONE_FIV_MIN.getName())){
            timeSpace = Const.ONE_FIV_MIN_SPACE;
            time = DateUtil.adjustTime(time, new Aggregation(AggregationType.m, ReqEnum.ONE_FIV_MIN.getValue()));
        }
        else if (resolute.equals(ReqEnum.ONE_HOU.getName())) {
            timeSpace = Const.ONE_HOU_SPACE;
            time = DateUtil.adjustTime(time, new Aggregation(AggregationType.h, ReqEnum.ONE_HOU.getValue()));
        } else if (resolute.equals(ReqEnum.ONE_DAY.getName())) {
            timeSpace = Const.ONE_DAY_SPACE;
            time = DateUtil.adjustTime(time, new Aggregation(AggregationType.d, ReqEnum.ONE_DAY.getValue()));
        }
        List<Kline> initList = new ArrayList<>();
        for (int i = 1; i < channelReq.getLimit(); i++) {
            BigDecimal[] data = DateUtil.getData();
            if (data != null) {
                time = time - timeSpace;
                // 清毫秒为0
                Kline kline = new Kline();
                kline.setId(time);
                kline.setHigh(data[0]);
                kline.setLow(data[1]);
                kline.setOpen(data[2]);
                kline.setClose(data[3]);
                kline.setVol(data[4]);
                initList.add(kline);
            }
        }
        Collections.reverse(initList);
        Map<String, Object> result = new HashMap<>();
        result.put("data",initList);
        result.put("type","req");
        result.put("id",channelReq.getChannel());
        WsResponse response = WsResultUtil.buildResponseByJson(result);
        Tio.send(channelContext, response);
    }

}
