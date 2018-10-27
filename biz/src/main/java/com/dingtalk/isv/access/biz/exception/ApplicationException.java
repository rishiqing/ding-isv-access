package com.dingtalk.isv.access.biz.exception;


public class ApplicationException extends RuntimeException {

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    protected ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApplicationException(ApplicationError applicationError, Throwable cause) {
        super(buildMessage(applicationError), cause);

    }

    public ApplicationException(ApplicationError applicationError){
        super(buildMessage(applicationError));
    }

    private static String buildMessage(ApplicationError applicationError) {
        return "http error, errcode is " + applicationError.getErrCode() + ", errmsg is " + applicationError.getErrMsg();
    }
}
