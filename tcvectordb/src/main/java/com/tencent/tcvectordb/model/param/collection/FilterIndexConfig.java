package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class FilterIndexConfig {
    private boolean filterAll;
    private List<String> fieldWithoutFilterIndex = new ArrayList<>();
    private int maxStrLen=32;

    public FilterIndexConfig(Builder builder) {
        this.filterAll = builder.filterAll;
        this.fieldWithoutFilterIndex = builder.fieldWithoutFilterIndex;
        this.maxStrLen = builder.maxStrLen;
    }


    public boolean isFilterAll() {
        return filterAll;
    }

    public void setFilterAll(boolean filterAll) {
        this.filterAll = filterAll;
    }

    public List<String> getFieldWithoutFilterIndex() {
        return fieldWithoutFilterIndex;
    }

    public void setFieldWithoutFilterIndex(List<String> fieldWithoutFilterIndex) {
        this.fieldWithoutFilterIndex = fieldWithoutFilterIndex;
    }

    public int getMaxStrLen() {
        return maxStrLen;
    }

    public void setMaxStrLen(int maxStrLen) {
        this.maxStrLen = maxStrLen;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    // 建造者模式
    public static class Builder {
        private boolean filterAll;
        private List<String> fieldWithoutFilterIndex;
        private int maxStrLen;

        private Builder() {

        }

        public Builder withFilterAll(boolean filterAll) {
            this.filterAll = filterAll;
            return this;
        }

        public Builder withFieldWithoutFilterIndex(List<String> fieldWithoutFilterIndex) {
            this.fieldWithoutFilterIndex = fieldWithoutFilterIndex;
            return this;
        }

        public Builder withMaxStrLen(int maxStrLen) {
            this.maxStrLen = maxStrLen;
            return this;
        }
        public FilterIndexConfig build() {
            return new FilterIndexConfig(this);
        }
    }
}
