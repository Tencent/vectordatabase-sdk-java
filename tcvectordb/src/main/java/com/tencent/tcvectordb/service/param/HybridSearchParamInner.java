package com.tencent.tcvectordb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.model.param.dml.HybridSearchParam;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class HybridSearchParamInner {
    private String database;
    private String collection;
    private HybridSearchParam hybridSearchParam;
    private ReadConsistencyEnum readConsistency = ReadConsistencyEnum.EVENTUAL_CONSISTENCY;

    public HybridSearchParamInner(String database, String collection, HybridSearchParam search, ReadConsistencyEnum readConsistency) {
        this.database = database;
        this.collection = collection;
        this.hybridSearchParam = search;
        this.readConsistency = readConsistency;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public HybridSearchParam getSearch() {
        return hybridSearchParam;
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
