package com.tencentcloudapi.model.param.collection;

/**
 * FilterIndex
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class FilterIndex extends IndexField {

    public FilterIndex(String fieldName, FieldType fieldType, IndexType indexType) {
        setFieldName(fieldName);
        setFieldType(fieldType);
        setIndexType(indexType);
    }
}
