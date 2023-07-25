package com.tencentcloudapi.model.param.collection.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: wlleiiwang
 * Date: 2023/7/25
 */
public class Index {

    private IndexField primaryField;
    private Map<String, IndexField> indexes;

    public Index(List<IndexField> indexs) {
        this.indexes = new HashMap<>();
        for (IndexField index : indexs) {
            add(index);
        }
    }

    public void add(IndexField index) {
        if (index.isPrimaryKey()) {
            this.primaryField = index;
        }
        this.indexes.put(index.getName(), index);
    }

    public Index indexName(String indexName) {
        this.indexes.remove(indexName);
        return this;
    }

    public List<IndexField> list() {
        return new ArrayList<>(this.indexes.values());
    }

    public IndexField getPrimaryField() {
        return primaryField;
    }
}
