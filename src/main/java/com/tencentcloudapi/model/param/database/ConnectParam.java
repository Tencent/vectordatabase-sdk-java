package com.tencentcloudapi.model.param.database;

/**
 * Parameters for client connection.
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class ConnectParam {
    private final String host;
    private final int port;
    private final String username;
    private final String key;
    private final int timeout;

    public ConnectParam(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.key = builder.key;
        this.timeout = builder.timeout;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
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
        private String host;
        private int port;
        private String username;
        private String key;
        private int timeout = 0;

        private Builder() {
        }

        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
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
            this.timeout = timeout;
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }
}
