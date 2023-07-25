package com.tencentcloudapi.model.param.index;

import com.tencentcloudapi.model.param.FieldType;
import com.tencentcloudapi.model.param.IndexType;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class FilterIndex extends IndexField {

    public FilterIndex(String name, FieldType fieldType, IndexType indexType) {
        super(name, fieldType, indexType);
    }
}
