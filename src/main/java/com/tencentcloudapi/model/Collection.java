package com.tencentcloudapi.model;

import com.tencentcloudapi.model.param.Filter;
import com.tencentcloudapi.model.param.HNSWSearchParams;

import java.util.List;

/**
 * VectorDB Document
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Collection {

    public void upsert(List<Document> documents, int timeout) {
    }

    public List<Document> query(List<String> documentIds, boolean retrieveVector, int limit, int timeout) {
        return null;
    }

    public List<List<Document>> search(List<List<Float>> vectors, Filter filter, HNSWSearchParams params,
                                       boolean retrieveVector, int limit, int timeout) {
        return null;
    }

    public List<Document> searchById(List<String> documentIds, Filter filter, HNSWSearchParams params,
                                     boolean retrieveVector, int limit, int timeout) {
        return null;
    }

    public void delete(List<String> documentIds, int timeout) {
    }
}
