package com.tencentcloudapi.model;

import com.tencentcloudapi.model.param.dml.Filter;
import com.tencentcloudapi.model.param.dml.HNSWSearchParams;

import java.util.List;

/**
 * VectorDB Document
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Collection {

    public void upsert(List<Document> documents) {
    }

    public List<Document> query(List<String> documentIds, boolean retrieveVector, int limit) {
        return null;
    }

    public List<List<Document>> search(List<List<Float>> vectors, Filter filter, HNSWSearchParams params,
                                       boolean retrieveVector, int limit) {
        return null;
    }

    public List<Document> searchById(List<String> documentIds, Filter filter, HNSWSearchParams params,
                                     boolean retrieveVector, int limit) {
        return null;
    }

    public void delete(List<String> documentIds) {
    }
}
