package com.tencent.tcvectordb.model.param.dml;

public class RerankParam {
    protected String method;

    public RerankParam(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
