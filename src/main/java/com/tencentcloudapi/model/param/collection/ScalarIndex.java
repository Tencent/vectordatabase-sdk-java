package com.tencentcloudapi.model.param.collection;

/**
 * ScalarIndex
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class ScalarIndex extends IndexField {

    public ScalarIndex(String fieldName, FieldType fieldType, IndexType indexType) {
        setFieldName(fieldName);
        setFieldType(fieldType);
        setIndexType(indexType);
    }
}
