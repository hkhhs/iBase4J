package org.shjr.iplat.core.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shjr.iplat.core.Constants;
import org.shjr.iplat.core.util.RedisUtil;

/**
 * 会话监听器
 * 
 * @author ShenHuaJie
 * @version $Id: SessionListener.java, v 0.1 2014年3月28日 上午9:06:12 ShenHuaJie Exp
 */
public class SessionListener implements HttpSessionListener {
	private Logger logger = LogManager.getLogger(SessionListener.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http
	 * .HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		session.setAttribute(Constants.WEBTHEME, "default");
		logger.info("创建了一个Session连接:[" + session.getId() + "]");
		RedisUtil.set(Constants.ALLUSER_NUMBER, getAllUserNumber(1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet
	 * .http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		logger.info("销毁了一个Session连接:[" + session.getId() + "]");
		RedisUtil.hdel(Constants.CURRENT_USER, session.getId());
		RedisUtil.set(Constants.ALLUSER_NUMBER, getAllUserNumber(-1));
	}

	private Long getAllUserNumber(int n) {
		return getAllUserNumber() + n;
	}

	/** 获取在线用户数量 */
	public static Long getAllUserNumber() {
		String v = RedisUtil.get(Constants.ALLUSER_NUMBER);
		if (v != null) {
			return Long.valueOf(v);
		}
		return 0L;
	}
}