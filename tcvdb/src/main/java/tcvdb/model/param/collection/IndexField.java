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

package tcvdb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * IndexField
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndexField {
    private String fieldName;
    private FieldType fieldType;
    private IndexType indexType;
    private MetricType metricType;
    private ParamsSerializer params;
    private FieldElementType fieldElementType;

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

    public ParamsSerializer getParams() {
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

    public void setParams(ParamsSerializer params) {
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

    public FieldElementType getFieldElementType() {
        return fieldElementType;
    }

    public void setFieldElementType(FieldElementType fieldElementType) {
        this.fieldElementType = fieldElementType;
    }
}
