package com.street.shop.pojo;

import lombok.Data;

import java.util.Map;


@Data
public class ResponseMessage {	
	private String statusCode;					// 状态码  (0:成功)	
	private String message;					// 响应的消息(对status的描述)
	private Map<String, Object> data;			//返回的数据
}
