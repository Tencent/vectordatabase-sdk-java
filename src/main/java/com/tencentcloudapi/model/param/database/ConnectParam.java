package com.tencentcloudapi.model.param.database;

import com.tencentcloudapi.exception.ErrorCode;
import com.tencentcloudapi.exception.ParamException;
import org.apache.commons.lang3.StringUtils;

import java.util.logging.Logger;

/**
 * Parameters for client connection.
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class ConnectParam {
    private final String url;
    private final String username;
    private final String key;
    private final int timeout;

    private static final Logger logger = Logger.getLogger(ConnectParam.class.getName());

    private ConnectParam(Builder builder) {
        this.url = builder.url;
        this.username = builder.username;
        this.key = builder.key;
        this.timeout = builder.timeout;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getKey() {
        return key;
    }

    public int getTimeout() {
        return timeout;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String url;
        private String username;
        private String key;
        private int timeout = 10;

        private Builder() {
        }

        public Builder withUrl(String host) {
            this.url = url;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withTimeout(int timeout) {
            if (timeout != 0) {
                this.timeout = timeout;
            }
            return this;
        }

        public ConnectParam build() throws ParamException {
            if (StringUtils.isEmpty(this.url)) {
                throw new ParamException("ConnectParam error: url is null");
            }
            if (StringUtils.isEmpty(this.username)) {
                throw new ParamException("ConnectParam error: username is null");
            }
            if (StringUtils.isEmpty(this.key)) {
                throw new ParamException("ConnectParam error: key is null");
            }
            return new ConnectParam(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }
}
