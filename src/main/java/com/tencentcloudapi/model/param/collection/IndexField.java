package com.tencentcloudapi.model.param.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * IndexField
 * User: wlleiiwang
 * Date: 2023/7/25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndexField {
    private String fieldName;
    private FieldType fieldType;
    private IndexType indexType;
    private MetricType metricType;
    private HNSWParams params;

    private Integer dimension;
    private Integer indexedCount;

    public IndexField() {
    }

//    public IndexField(String fieldName, Integer dimension,
//        IndexType indexType, MetricType metricType, HNSWParams params) {
//        this.fieldName = fieldName;
//        this.indexType = indexType;
//        this.dimension = dimension;
//        this.metricType = metricType;
//        this.params = params;
//        this.fieldType = FieldType.Vector;
//    }

    public IndexType getIndexType() {
        return indexType;
    }

    @JsonIgnore
    public boolean isVectorField() {
        return FieldType.Vector.equals(this.fieldType);
    }

    @JsonIgnore
    public boolean isPrimaryKey() {
        return IndexType.PRIMARY_KEY.equals(this.indexType);
    }

    public String getFieldName() {
        return fieldName;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public Integer getDimension() {
        return dimension;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public HNSWParams getParams() {
        return params;
    }

    public Integer getIndexedCount() {
        return indexedCount;
    }

    public void setIndexedCount(Integer indexedCount) {
        this.indexedCount = indexedCount;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public void setParams(HNSWParams params) {
        this.params = params;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public void setIndexType(IndexType indexType) {
        this.indexType = indexType;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
