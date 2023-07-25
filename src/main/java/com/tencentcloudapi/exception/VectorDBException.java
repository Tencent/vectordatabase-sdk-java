package com.tencentcloudapi.exception;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class VectorDBException extends RuntimeException {
    private int code;

    public VectorDBException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
