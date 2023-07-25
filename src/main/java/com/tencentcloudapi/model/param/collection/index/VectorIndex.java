package com.tencentcloudapi.model.param.collection.index;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class VectorIndex  extends IndexField {
    private int dimension;
    private MetricType metricType;
    private HNSWParams params;

    public VectorIndex(String name, int dimension,
                       IndexType indexType, MetricType metricType,
                       HNSWParams params) {
        super(name, FieldType.Vector, indexType);
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
