package com.tencentcloudapi.model.param.collection;

import com.tencentcloudapi.model.param.collection.*;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class VectorIndex  extends IndexField {
    private int dimension;
    private MetricType metricType;
    private HNSWParams params;

    public VectorIndex(String fieldName, int dimension,
                       IndexType indexType, MetricType metricType,
                       HNSWParams params) {
        super(fieldName, FieldType.Vector, indexType);
        this.dimension = dimension;
        this.metricType = metricType;
        this.params = params;
    }

    public int getDimension() {
        return dimension;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public HNSWParams getParams() {
        return params;
    }
}
