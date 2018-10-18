package com.dingtalk.isv.access.biz.exception;

/**
 * 可以报出给前台提示的错误，包含errCode和errMsg两部分
 * @author Wallace Mao
 * Date: 2018-03-01 14:46
 */
public enum ApplicationError {

    //  系统错误
    SYS_ERROR(-1L, "system error");

    private final Long errCode;
    private final String errMsg;

    ApplicationError(Long errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Long getErrCode() {
        return this.errCode;
    }
    public String getErrMsg() {
        return this.errMsg;
    }
}
