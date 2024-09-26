package com.tencent.tcvectordb.model.param.dml;

import java.util.List;

public class WeightRerankParam extends RerankParam{
    private List<String> fieldList;
    private List<Number> weight;
    public WeightRerankParam(List<String> fieldList, List<Number> weight) {
        super("weighted");
        this.fieldList = fieldList;
        this.weight = weight;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public List<Number> getWeight() {
        return weight;
    }

    public void setWeight(List<Number> weight) {
        this.weight = weight;
    }

    // 建造者模式

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder {
        private List<String> fieldList;
        private List<Number> weight;
        public Builder withFieldList(List<String> fieldList) {
            this.fieldList = fieldList;
            return this;
        }
        public Builder withWeight(List<Number> weight) {
            this.weight = weight;
            return this;
        }
        public WeightRerankParam build() {
            return new WeightRerankParam(fieldList, weight);
        }
    }
}
