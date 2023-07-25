package com.tencentcloudapi.exception;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class NoConnectException extends VectorDBException {
    public NoConnectException(String message, int code) {
        super(message, code);
    }
}
