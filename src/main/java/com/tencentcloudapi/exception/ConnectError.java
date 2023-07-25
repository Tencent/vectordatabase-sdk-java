package com.tencentcloudapi.exception;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class ConnectError extends VectorDBException {
    public ConnectError(String message, int code) {
        super(message, code);
    }
}
