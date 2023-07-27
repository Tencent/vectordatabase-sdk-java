package com.tencentcloudapi.model.param.collection;

/**
 * VectorIndex
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class VectorIndex  extends IndexField {


    public VectorIndex(String fieldName, Integer dimension,
                       IndexType indexType, MetricType metricType,
                       HNSWParams params) {
        setFieldName(fieldName);
        setFieldType(FieldType.Vector);
        setIndexType(indexType);
        setDimension(dimension);
        setMetricType(metricType);
        setParams(params);
    }
}
