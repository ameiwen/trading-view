/**
 * 
 */
package com.yaa.trading.comm;

import java.util.Arrays;
import java.util.List;

public class Const {

	public static List<String> times = Arrays.asList("1min,5min,15min,1hour,1day".split(","));

	public static List<String> symbols = Arrays.asList("btcusdt,ethusdt".split(","));
	/**
	 * 用于Handler的标志
	 */
	public static final String KLINE_SUB_CMD = "sub";

	public static final String KLINE_UNSUB_CMD = "unsub";

	public static final String KLINE_REQ_CMD = "req";


	//时间单位间隔
	public static final Integer ONE_MIN_SPACE = 60 * 1000;

	public static final Integer FIV_MIN_SPACE = 60 * 1000 * 5;

	public static final Integer ONE_FIV_MIN_SPACE = 60 * 1000 * 15;

	public static final Integer ONE_HOU_SPACE = 60 * 60 * 1000;

	public static final Integer ONE_DAY_SPACE = 60 * 60 * 24 * 1000;

}
