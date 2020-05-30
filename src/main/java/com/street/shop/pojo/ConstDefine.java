package com.street.shop.pojo;

public class ConstDefine {
	
	public static final long defer_operation_expite_in = 30000;

	public static final String JWT_AUTH_HEADER = "Authorization";

	public static final String ERROR  = "error:";	
	public static final String SUCCESS  = "success";
	
	public static final int VERIFYCODE_EXPIRE_TIME = 15 * 60;		//短信有效期
	
	public static final int MAX_PAGE_SIZE = 128;
	
	public static final int PLATFORM_RATIO = 5;		//平台的提成费率

	public static final int MAX_FREE_PUBLISH_SERVICE_ITEM_NUM  = 3;		//免费发布的服务项目数量


	public static final String SUCCESS_CODE = "0";

	public static final String PHONE_INVALID_CODE = "100";
	public static final String PHONE_INVALID_MSG = "手机号码格式不正确!";

	public static final String USER_REGIST_FAIL_CODE = "101";	//注册失败

	public static final String USER_LOGIN_FAIL_CODE = "101";	//登录失败
	public static final String RESET_PASS_FAIL_CODE = "102";	//重置密码失败

	public static final String TOKEN_INVALID_CODE = "103";	//令牌无效

	public static final String DEL_USER_FAIL_CODE = "104";	//删除用户失败


	public static final int productNameCellIndex = 0;
	public static final int productUniqueCodeCellIndex = 1;

	public static final int unitSpecUniqueCodeCellIndex = 3;
	public static final int priceCellIndex = 4;
	public static final int offlinePriceCellIndex = 5;


}
