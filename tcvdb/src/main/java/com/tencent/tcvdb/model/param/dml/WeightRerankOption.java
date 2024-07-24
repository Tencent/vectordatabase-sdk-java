package com.tencent.tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeightRerankOption extends RerankOption{

    private List<String> fieldList;
    private List<Double> weight;
    public WeightRerankOption(List<String> fieldList, List<Double> weight) {
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

    public List<Double> getWeight() {
        return weight;
    }

    public void setWeight(List<Double> weight) {
        this.weight = weight;
    }

    // 建造者模式

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder {
        private List<String> fieldList;
        private List<Double> weight;
        public Builder withFieldList(List<String> fieldList) {
            this.fieldList = fieldList;
            return this;
        }
        public Builder withWeight(List<Double> weight) {
            this.weight = weight;
            return this;
        }
        public WeightRerankOption build() {
            return new WeightRerankOption(fieldList, weight);
        }
    }
}
