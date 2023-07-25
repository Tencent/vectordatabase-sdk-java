package com.tencentcloudapi.model.param.collection;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class IndexField {
    private String fieldName;
    private FieldType fieldType;
    private IndexType indexType;

    public IndexField(String fieldName, FieldType fieldType, IndexType indexType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.indexType = indexType;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public boolean isVectorField() {
        return FieldType.Vector.equals(this.fieldType);
    }

    public boolean isPrimaryKey() {
        return IndexType.PRIMARY_KEY.equals(this.indexType);
    }

    public String getFieldName() {
        return fieldName;
    }

    public FieldType getFieldType() {
        return fieldType;
    }
}
