package com.nevs.web.exception;

/**
 * @author YETA
 * 部门操作异常
 * @date 2018/08/29/15:04
 */
public class DepartmentException extends RuntimeException {

    public DepartmentException() {
    }

    public DepartmentException(String message) {
        super(message);
    }

    public DepartmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
