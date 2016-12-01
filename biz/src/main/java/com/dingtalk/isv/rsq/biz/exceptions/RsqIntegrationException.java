package com.dingtalk.isv.rsq.biz.exceptions;

/**
 * 日事清集成时发生的异常
 * Created by Wallace on 2016/11/30.
 */
public class RsqIntegrationException extends RuntimeException {
    public RsqIntegrationException() {
    }

    public RsqIntegrationException(String message) {
        super(message);
    }

    public RsqIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RsqIntegrationException(Throwable cause) {
        super(cause);
    }

    public RsqIntegrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
