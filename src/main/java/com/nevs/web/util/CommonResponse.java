package com.nevs.web.util;

/**
 * @author YETA
 * 前端请求返回统一
 * @date 2018/08/26/14:20
 */
public class CommonResponse {

    /**
     * 请求是否成功
     */
    private boolean success = true;

    /**
     * 状态码
     * 1：成功
     * 2：token因为各种原因验证失败
     * 3：错误
     * 4：异常
     */
    private Integer statusCode = 1;

    /**
     * 请求返回数据
     */
    private Object data;

    /**
     * 请求返回错误信息
     */
    private String error;

    public CommonResponse() {
    }

    public CommonResponse(Object data) {
        this.data = data;
    }

    public CommonResponse(boolean success, Integer statusCode, String error) {
        this.success = success;
        this.statusCode = statusCode;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "success=" + success +
                ", statusCode=" + statusCode +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
