package com.nevs.web.exception;

/**
 * @author YETA
 * 积分相关异常
 * @date 2018/08/27/15:28
 */
public class IntegralException extends RuntimeException {

    public IntegralException() {
        super();
    }

    public IntegralException(String message) {
        super(message);
    }

    public IntegralException(String message, Throwable cause) {
        super(message, cause);
    }
}
