package com.tencentcloudapi.model.param;

import java.util.Collections;
import java.util.List;

/**
 * VectorDB Filter
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Filter {

    private StringBuffer condBuffer;

    public Filter(String cond) {
        this.condBuffer = new StringBuffer(cond);
    }

    public Filter and(String cond) {
        this.condBuffer.append(" and ").append(cond);
        return this;
    }

    public Filter or(String cond) {
        this.condBuffer.append(" or ").append(cond);
        return this;
    }

    public Filter andNot(String cond) {
        this.condBuffer.append(" and not ").append(cond);
        return this;
    }

    public Filter orNot(String cond) {
        this.condBuffer.append(" or not ").append(cond);
        return this;
    }

    public String in(String key, List<String> values) {
        return String.format("%s in(%s)", key,  String.join(",", values));
    }

    public String getCond() {
        return condBuffer.toString();
    }
}
