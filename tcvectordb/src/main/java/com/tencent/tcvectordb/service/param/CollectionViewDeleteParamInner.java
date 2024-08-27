package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.param.dml.CollectionViewConditionParam;

public class CollectionViewDeleteParamInner {

    private String database;
    private String collectionView;
    private CollectionViewConditionParam query;

    public CollectionViewDeleteParamInner(String database, String collectionView, CollectionViewConditionParam query) {
        this.database = database;
        this.collectionView = collectionView;
        this.query = query;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollectionView() {
        return collectionView;
    }

    public CollectionViewConditionParam getQuery() {
        return query;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "QueryParam error: %s", e.getMessage()));
        }
    }
}
