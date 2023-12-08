package com.tencent.tcvectordb.model.param.dml;

public class RerankOption {
    private boolean enable;
    private double expectRecallMultiples;

    public RerankOption(boolean enable, double expectRecallMultiples) {
        this.enable = enable;
        this.expectRecallMultiples = expectRecallMultiples;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public double getExpectRecallMultiples() {
        return expectRecallMultiples;
    }

    public void setExpectRecallMultiples(double expectRecallMultiples) {
        this.expectRecallMultiples = expectRecallMultiples;
    }
}
