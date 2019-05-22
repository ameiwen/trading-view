package com.yaa.trading.util;

import com.alibaba.fastjson.JSON;
import com.yaa.trading.comm.ServerConfig;
import org.tio.websocket.common.WsResponse;

import java.io.UnsupportedEncodingException;

public class WsResultUtil {

    public static WsResponse buildResponseByJson(Object obj){
        WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(obj), ServerConfig.CHARSET);
        return wsResponse;
    }

    public static WsResponse buildResponseByByte(Object obj){
        WsResponse wsResponse = null;
        try {
            wsResponse = WsResponse.fromBytes(JSON.toJSONString(obj).getBytes(ServerConfig.CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return wsResponse;
    }

    public static WsResponse buildResponseByText(String message){
        WsResponse  wsResponse = WsResponse.fromText(message,ServerConfig.CHARSET);
        return wsResponse;
    }

}
