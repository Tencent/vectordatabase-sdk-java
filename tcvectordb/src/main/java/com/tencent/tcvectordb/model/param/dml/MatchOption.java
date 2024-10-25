package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.exception.ParamException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * MatchOption for sparse vector search
 * Params:
 *     fieldName: String, field name of the sparse vector field, value must be "sparse_vector".
 *     data: List<List<List<Object>>>, sparse vector data, can user MatchOption.newBuilder().withData(List<List<Pair<Long, Float>>>) to use sparse vector data.
 *     Limit: Integer, search limit
 * eg:
 *     MatchOption option = new MatchOption.Builder().withFieldName("sparse_vector").withData(sparseVector).build();
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchOption {
    private String fieldName;
    private List<List<List<Object>>> data;
    private Integer limit;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<List<List<Object>>> getData() {
        return data;
    }

    public void setData(List<List<List<Object>>> data) {
        this.data = data;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public MatchOption(Builder builder) {
        this.fieldName = builder.fieldName;
        this.data = builder.data;
        this.limit = builder.limit;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String fieldName;
        private List<List<List<Object>>> data;
        private Integer limit;

        private Builder() {
        }

        public Builder withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder withData(List<List<Pair<Long, Float>>> data){
            List<List<List<Object>>> sparseData = new ArrayList<>();
            for (List<Pair<Long, Float>> dataItem : data){
                List<List<Object>> pairsList = new ArrayList<>();
                for (Pair<Long, Float> dataItemItem : dataItem){
                    List<Object> pairTmp = new ArrayList<>();
                    pairTmp.add(dataItemItem.getLeft());
                    pairTmp.add(dataItemItem.getRight());
                    pairsList.add(pairTmp);
                }
                sparseData.add(pairsList);
            }
            this.data = sparseData;
            return this;
        }

        public Builder withLimit(Integer limit){
            this.limit = limit;
            return this;
        }

        public MatchOption build() {
            if (fieldName == null || data.isEmpty()){
                throw new ParamException("RetrieveOption error: fieldName or data is null");
            }
            return new MatchOption(this);
        }
    }

}
