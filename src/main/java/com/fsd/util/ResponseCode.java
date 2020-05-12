package com.fsd.util;

public class ResponseCode {
    protected ResponseCode() {}

	//成功
    public static final int SUCCESS = 200;
    //错误请求
    public static final int BAD_REQUEST = 400;
    //禁止
    public static final int FORBIDDEN = 403;
    //没有找到
    public static final int NOT_FOUND = 404;
	//服务不可用
    public static final int SERVICE_UNAVAILABLE = 500;
    //文件格式错误
    public static final int FILE_FORMAT_ERROR = 6001;
    //数据访问失败
    public static final int ERROR_ACCESS_DB = 6002;

}