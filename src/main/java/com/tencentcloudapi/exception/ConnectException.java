package com.tencentcloudapi.exception;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class ConnectException extends VectorDBException {
    public ConnectException(String message, int code) {
        super(message, code);
    }
}
