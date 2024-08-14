package com.tencent.tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRFRerankOption extends RerankOption{

    private int rrfK;
    public RRFRerankOption(int rrfK) {
        super("rrf");
        this.rrfK = rrfK;
    }

    public int getRrfK() {
        return rrfK;
    }

    public void setRrfK(int rrfK) {
        this.rrfK = rrfK;
    }

    // 建造者模式

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder {
        private int rrfK;
        public Builder withRRFK(int rrfK){
            this.rrfK = rrfK;
            return this;
        }
        public RRFRerankOption build() {
            return new RRFRerankOption(rrfK);
        }
    }
}
