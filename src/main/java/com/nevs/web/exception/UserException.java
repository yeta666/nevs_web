package com.nevs.web.exception;

/**
 * @author YETA
 * 用户相关异常
 * @date 2018/08/27/15:28
 */
public class UserException extends RuntimeException {

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
