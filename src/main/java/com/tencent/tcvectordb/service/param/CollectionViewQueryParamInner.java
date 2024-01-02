package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.param.dml.CollectionViewConditionParam;
import com.tencent.tcvectordb.model.param.dml.CollectionViewQueryParam;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionViewQueryParamInner {

    private String database;
    private String collectionView;
    private ReadConsistencyEnum readConsistency;
    private CollectionViewQueryParam query;

    public CollectionViewQueryParamInner(String database, String collectionView, CollectionViewQueryParam query, ReadConsistencyEnum readConsistency) {
        this.database = database;
        this.collectionView = collectionView;
        this.readConsistency = readConsistency;
        this.query = query;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollectionView() {
        return collectionView;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public CollectionViewQueryParam getQuery() {
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
