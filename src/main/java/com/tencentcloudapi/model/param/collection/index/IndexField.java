package com.tencentcloudapi.model.param.collection.index;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class IndexField {
    private String name;
    private FieldType fieldType;
    private IndexType indexType;

    public IndexField(String name, FieldType fieldType, IndexType indexType) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public FieldType getFieldType() {
        return fieldType;
    }
}
