package com.nevs.web.exception;

/**
 * @author YETA
 * 车辆订单相关异常
 * @date 2018/08/27/15:28
 */
public class OrderException extends RuntimeException {

    public OrderException() {
        super();
    }

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
