package com.tencent.tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContextResult {
    private List<String> pre;
    private List<String> next;
    private SourceInfo sourceInfo;

    public ContextResult() {

    }
    public ContextResult(Builder builder) {
        this.pre = builder.pre;
        this.next = builder.next;
        this.sourceInfo = builder.sourceInfo;
    }

    public List<String> getPre() {
        return pre;
    }

    public void setPre(List<String> pre) {
        this.pre = pre;
    }

    public List<String> getNext() {
        return next;
    }

    public void setNext(List<String> next) {
        this.next = next;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(SourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    // 建造者模式
    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder {
        private List<String> pre;
        private List<String> next;
        private SourceInfo sourceInfo;

        public Builder() {

        }

        public Builder withPre(List<String> pre) {
            this.pre = pre;
            return this;
        }

        public Builder withNext(List<String> next) {
            this.next = next;
            return this;
        }

        public Builder withSourceInfo(SourceInfo sourceInfo) {
            this.sourceInfo = sourceInfo;
            return this;
        }

        public ContextResult build() {
            return new ContextResult(this);
        }
    }
}
