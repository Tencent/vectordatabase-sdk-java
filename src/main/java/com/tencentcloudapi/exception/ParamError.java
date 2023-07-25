package com.tencentcloudapi.exception;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class ParamError extends VectorDBException {
    public ParamError(String message, int code) {
        super(message, code);
    }
}
