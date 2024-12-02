package com.tencent.tcvectordb.model.param.collectionView;


import com.tencent.tcvectordb.model.param.enums.ParsingTypeEnum;

public class ParsingProcessParam {
    private String parsingType;

    public String getParsingType() {
        return parsingType;
    }

    public void setParsingType(String parsingType) {
        this.parsingType = parsingType;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    // builder
    public static class Builder {
        private String parsingType;

        public Builder withParsingType(ParsingTypeEnum parsingType) {
            this.parsingType = parsingType.getValue();
            return this;
        }

        public ParsingProcessParam build() {
            ParsingProcessParam parsingProcessParam = new ParsingProcessParam();
            parsingProcessParam.parsingType = this.parsingType;
            return parsingProcessParam;
        }
    }
}
