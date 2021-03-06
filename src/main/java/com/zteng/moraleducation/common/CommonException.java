package com.zteng.moraleducation.common;

/**
 * @Description: AppWebException
 * @Copyright: Copyright (c) 2016
 * Author lv bin
 * @date 2017/3/22 10:10
 * version V1.0.0
 */
public class CommonException extends RuntimeException{

    private static final long serialVersionUID = -8198281171334131008L;

    private int errCode;

    public CommonException() {}

    public CommonException(int errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public CommonException(String message) {
        super(message);
        this.errCode = ResultCode.FAILED.getCode();
    }

    public CommonException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.errCode = resultCode.getCode();
    }

    public int getErrCode() {
        return this.errCode;
    }
}
