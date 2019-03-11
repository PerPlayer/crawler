/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.crawler.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class B extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public B() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static B error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static B error(String msg) {
		return error(500, msg);
	}
	
	public static B error(int code, String msg) {
		B b = new B();
		b.put("code", code);
		b.put("msg", msg);
		return b;
	}

	public static B ok(String msg) {
		B b = new B();
		b.put("msg", msg);
		return b;
	}
	
	public static B ok(Map<String, Object> map) {
		B b = new B();
		b.putAll(map);
		return b;
	}
	
	public static B ok() {
		return new B();
	}

	@Override
	public B put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
