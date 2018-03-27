package com.cv4j.proxy.web.dto;

import java.util.HashMap;

/**
 *
 * 用户返回结果对象
 *
 */
public class ResultMap extends HashMap<String, Object>{
	private static final long serialVersionUID = 1L;
	
	private ResultMap() { }

	/**
	 * 返回成功
	 */
	public static ResultMap ok() {
		return ok(200, "success");
	}

	/**
	 * 返回成功
	 */
	public static ResultMap ok(int code, String message) {
		ResultMap resultMap = new ResultMap();
		resultMap.put("code", code);
		resultMap.put("msg", message);
		return resultMap;
	}
	
	/**
	 * 返回失败
	 */
	public static ResultMap failure() {
		return failure(500, "failure");
	}

	/**
	 * 返回失败
	 */
	public static ResultMap failure(int code, String message) {
		ResultMap resultMap = new ResultMap();
		resultMap.put("code", code);
		resultMap.put("msg", message);
		return resultMap;
	}
	
	/**
	 * 放入object
	 */
	@Override
	public ResultMap put(String key, Object object){
		super.put(key, object);
		return this;
	}
}