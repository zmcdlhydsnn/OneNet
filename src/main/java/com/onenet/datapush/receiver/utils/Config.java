package com.onenet.datapush.receiver.utils;

import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {
	private final static Properties properties;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(Config.class);
	static {
		InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties");
		properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			logger.error("init config error", e);
		}
	}

	/**
	 * 读取以逗号分割的字符串，作为字符串数组返回
	 *
	 * @param conf
	 * @return
	 */
	public static List<String> getStringList(String conf) {
		return Arrays.asList(StringUtils.split(properties.getProperty(conf), ","));
	}

	/**
	 * 读取字符串
	 *
	 * @param conf
	 * @return
	 */
	public static String getString(String conf) {
		return properties.getProperty(conf);
	}

	/**
	 * 读取整数
	 *
	 * @param conf
	 * @return
	 */
	public static int getInt(String conf) {
		int ret = 0;
		try {
			ret = Integer.parseInt(getString(conf));
		} catch (NumberFormatException e) {
			logger.error("format error", e);
		}
		return ret;
	}

	/**
	 * 读取布尔值
	 *
	 * @param conf
	 * @return
	 */
	public static boolean getBoolean(String conf) {
		return Boolean.parseBoolean(getString(conf));
	}
}
