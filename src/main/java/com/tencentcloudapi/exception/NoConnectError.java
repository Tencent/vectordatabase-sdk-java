package com.tencentcloudapi.exception;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class NoConnectError extends VectorDBException {
    public NoConnectError(String message, int code) {
        super(message, code);
    }
}
