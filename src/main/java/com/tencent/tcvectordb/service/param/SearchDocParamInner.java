package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.param.dml.SearchByContentsParam;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;

/**
 * Inner Search Doc Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchDocParamInner {
    private String database;
    private String collectionView;
    private SearchByContentsParam search;
    private ReadConsistencyEnum readConsistency = ReadConsistencyEnum.EVENTUAL_CONSISTENCY;

    public SearchDocParamInner(String database, String collectionView, SearchByContentsParam search, ReadConsistencyEnum readConsistency) {
        this.database = database;
        this.collectionView = collectionView;
        this.search = search;
        this.readConsistency = readConsistency;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollectionView() {
        return collectionView;
    }

    public SearchByContentsParam getSearch() {
        return search;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "InsertParam error: %s", e.getMessage()));
        }
    }
}
