package com.yaa.trading.comm;

import org.tio.server.ServerGroupContext;
import org.tio.utils.time.Time;

public abstract class ServerConfig {
	
	public static final String CHARSET = "utf-8";

	/**
	 * 心跳请求
	 */
	public static final String  HEART_BEAT = "ping";


	/**
	 * 监听的ip null表示监听所有，并不指定ip
	 */
	public static final String SERVER_IP = null;

	/**
	 * 监听端口
	 */
	public static final int SERVER_PORT = 9326;

	/**
	 * 心跳超时时间，单位：毫秒
	 */
	public static final int HEARTBEAT_TIMEOUT = 1000 * 60;


	/**
	 * 给MsgUtil hold住实例，直接调用
	 */
	public static ServerGroupContext groupContext;

	/**
	 * ip数据监控统计，时间段
	 *
	 */
	public interface IpStatDuration {
		Long DURATION_1 = Time.MINUTE_1 * 5;
		Long[] IP_STAT_DURATIONS = new Long[] { DURATION_1 };
	}

}
