package com.yaa.trading.server.handler;

import com.yaa.trading.comm.Aggregation;
import com.yaa.trading.comm.AggregationType;
import com.yaa.trading.comm.ReqEnum;
import com.yaa.trading.server.bean.ChannelReq;
import com.yaa.trading.server.bean.Kline;
import com.yaa.trading.server.processor.ServerProcessor;
import com.yaa.trading.util.DateUtil;
import com.yaa.trading.util.WsResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class KlineUpdateHandler extends ServerProcessor {

	private static Logger logger = LoggerFactory.getLogger(KlineUpdateHandler.class);

	private static final Lock lock = new ReentrantLock();

	private static final ConcurrentHashMap<String, TimerTread> timerMap = new ConcurrentHashMap<>();

	/**
	 * @param wsRequest
	 * @param channelReq
	 * @param channelContext
	 * @return
	 * @throws Exception
	 */
	@Override
	public void handleMessage(WsRequest wsRequest, ChannelReq channelReq, ChannelContext channelContext) {
		String channel = channelReq.getChannel();
		logger.info("Channel[" + channel + "] subscribed");
		lock.lock();
		try {
			//保持一个线程运行
			TimerTread timerTread = timerMap.get(channel);
			if (timerTread == null) {
				timerTread = new TimerTread(1000l, channelContext, channelReq);
				timerTread.setDaemon(true);
				timerTread.start();
				timerMap.putIfAbsent(channel, timerTread);
			}
		} finally {
			lock.unlock();
		}
	}

	class TimerTread extends Thread {
		private Long sleepMills = 0l;
		private ChannelReq channelReq;
		private ChannelContext channelContext;

		public TimerTread(Long sleepMills, ChannelContext channelContext, ChannelReq channelReq) {
			this.sleepMills = sleepMills;
			this.channelReq = channelReq;
			this.channelContext = channelContext;
		}

		@Override
		public void run() {
			while (true) {
				try {
					SetWithLock<ChannelContext> o = Tio.getChannelContextsByGroup(channelContext.getGroupContext(), channelReq.getChannel());
					int count = 0;
					if (o != null) {
						count = o.getObj().size();
					}
					logger.info("当前订阅数量：" + count);
					String resolute = channelReq.getChannel().split("[.]")[1];
					Long time = System.currentTimeMillis();
					if (resolute.equals(ReqEnum.ONE_MIN.getName())) {
						time = DateUtil.adjustTime(time, new Aggregation(AggregationType.m, ReqEnum.ONE_MIN.getValue()));
					} else if (resolute.equals(ReqEnum.FIV_MIN.getName())) {
						time = DateUtil.adjustTime(time, new Aggregation(AggregationType.m, ReqEnum.FIV_MIN.getValue()));
					} else if(resolute.equals(ReqEnum.ONE_FIV_MIN.getName())){
						time = DateUtil.adjustTime(time, new Aggregation(AggregationType.m, ReqEnum.ONE_FIV_MIN.getValue()));
					} else if (resolute.equals(ReqEnum.ONE_HOU.getName())) {
						time = DateUtil.adjustTime(time, new Aggregation(AggregationType.h, ReqEnum.ONE_HOU.getValue()));
					} else if (resolute.equals(ReqEnum.ONE_DAY.getName())) {
						time = DateUtil.adjustTime(time, new Aggregation(AggregationType.d, ReqEnum.ONE_DAY.getValue()));
					}
					BigDecimal[] data = DateUtil.getData();
					Kline kline = new Kline();
					if (data != null) {
						kline = new Kline();
						kline.setId(time);
						kline.setHigh(data[0]);
						kline.setLow(data[1]);
						kline.setOpen(data[2]);
						kline.setClose(data[3]);
						kline.setVol(data[4]);
					}
					Map<String, Object> result = new HashMap<>();
					result.put("data", kline);
					result.put("type", "sub");
					result.put("id", channelReq.getChannel());
					WsResponse response = WsResultUtil.buildResponseByJson(result);
					Tio.sendToGroup(channelContext.getGroupContext(), channelReq.getChannel(), response);
					Thread.sleep(sleepMills);
				} catch (Exception e) {
					logger.error("error", e);
				}
			}
		}
	}

}
