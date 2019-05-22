package com.yaa.trading.server.handler;

import com.alibaba.fastjson.JSON;
import com.yaa.trading.comm.Const;
import com.yaa.trading.comm.ServerConfig;
import com.yaa.trading.server.bean.ChannelReq;
import com.yaa.trading.server.processor.ServerProcessor;
import com.yaa.trading.util.WsResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends AbsServerHandler implements IWsMsgHandler {

	private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	public static ServerHandler me = new ServerHandler();

	private static final Map<String, ServerProcessor> handlerMap = new ConcurrentHashMap<>();

	static {
		for(String time : Const.times){
			for(String symbol : Const.symbols) {
				handlerMap.put("req." + time + "." + symbol, new KlineHisHandler());
				handlerMap.put("sub." + time + "." + symbol, new KlineUpdateHandler());
			}
		}
	}

	/**
	 * 字符消息过来后会走这个方法
	 */
	@Override
	public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
		ChannelReq channelReq = JSON.parseObject(text, ChannelReq.class);
		if (channelReq != null) {
			if (ServerConfig.HEART_BEAT.equals(channelReq.getCmd())) {
				Map<String, Object> heart = new HashMap<>();
				heart.put("pong", new Date().getTime());
				Tio.send(channelContext, WsResultUtil.buildResponseByJson(heart));
				logger.info("heart beat:" + text);
				return null;
			}
			ServerProcessor server = handlerMap.get(channelReq.getChannel());
			if (server != null) {
				if (channelReq.getCmd().equals(Const.KLINE_SUB_CMD)) {
					Tio.bindGroup(channelContext, channelReq.getChannel());
				}else if(channelReq.getCmd().equals(Const.KLINE_UNSUB_CMD)){
					Tio.unbindGroup(channelReq.getChannel(),channelContext);
				}
				server.handleMessage(wsRequest, channelReq, channelContext);
			}
		}
		return null;
	}

}
