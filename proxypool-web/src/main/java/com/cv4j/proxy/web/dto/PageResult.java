package com.cv4j.proxy.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * 用于存储查询结果
 *
 * @param <T>
 */
@Setter
@Getter
public class PageResult<T> {
	
	private int code; //状态码, 0表示成功
	
	private String msg;  //提示信息

	private long count; // 总数量, bootstrapTable是total

	private List<T> data; // 当前数据, bootstrapTable是rows

	public PageResult() {
	}

	public PageResult(long total, List<T> rows) {
		this.code = 200;
		this.msg = "";
		this.count = total;
		this.data = rows;
	}

}
