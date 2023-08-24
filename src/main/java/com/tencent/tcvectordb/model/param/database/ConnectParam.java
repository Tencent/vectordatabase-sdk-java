/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.tcvectordb.model.param.database;

import com.tencent.tcvectordb.exception.ParamException;
import org.apache.commons.lang3.StringUtils;

/**
 * Parameters for client connection.
 */
public class ConnectParam {
    private final String url;
    private final String username;
    private final String key;
    private final int timeout;

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

        public Builder withUrl(String url) {
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
