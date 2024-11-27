/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.tcvectordb.model.param.collection;

/**
 * VectorIndex
 */
public class VectorIndex  extends IndexField {


    public VectorIndex(String fieldName, Integer dimension,
                       IndexType indexType, MetricType metricType,
                       ParamsSerializer params) {
        setFieldName(fieldName);
        setFieldType(FieldType.Vector);
        setIndexType(indexType);
        setDimension(dimension);
        setMetricType(metricType);
        setParams(params);
    }

    public VectorIndex(String fieldName, Integer dimension, FieldType fieldType,
                       IndexType indexType, MetricType metricType,
                       ParamsSerializer params) {
        setFieldName(fieldName);
        setFieldType(fieldType);
        setIndexType(indexType);
        setDimension(dimension);
        setMetricType(metricType);
        setParams(params);
    }

    public VectorIndex(MetricType metricType, ParamsSerializer params) {
        setFieldName("vector");
        setFieldType(FieldType.Vector);
//        setIndexType(indexType);
//        setDimension(dimension);
        setMetricType(metricType);
        setParams(params);
    }
}
