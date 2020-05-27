package com.street.shop.pojo;

import lombok.Data;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;


@Data
public class CommonTask {	
	private String commandCode;							// 操作码
	private Map<String, Object> parameter;			// 操作参数		
	private DeferredResult<ResponseMessage> deferredResult; 		// 异步任务结果

}
