package com.tencent.tcvectordb.model.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.tcvectordb.exception.ParamException;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.service.Stub;
import com.tencent.tcvectordb.service.param.*;

import java.util.Date;
import java.util.List;

public abstract class BaseCollection {
    @JsonIgnore
    protected Stub stub;
    protected String database;
    protected String collection;
    protected int replicaNum = 2;
    protected int shardNum = 1;
    protected String description;
    protected List<IndexField> indexes;
    protected String createTime;
    @JsonIgnore
    protected ReadConsistencyEnum readConsistency;
    private long documentCount;
    private Collection.IndexStatus indexStatus;
    private List<String> alias;

    public void setStub(Stub stub) {
        this.stub = stub;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCollection() {
        return collection;
    }

    public int getReplicaNum() {
        return replicaNum;
    }

    public int getShardNum() {
        return shardNum;
    }

    public String getDescription() {
        return description;
    }

    public List<IndexField> getIndexes() {
        return indexes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public long getDocumentCount() {
        return documentCount;
    }

    public Collection.IndexStatus getIndexStatus() {
        return indexStatus;
    }

    public List<String> getAlias() {
        return alias;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public void setReadConsistency(ReadConsistencyEnum readConsistency) {
        this.readConsistency = readConsistency;
    }

    public AffectRes upsert(InsertParam param) throws VectorDBException {
        InsertParamInner insertParam = new InsertParamInner(
                database, collection, param.getDocuments());
        return this.stub.upsertDocument(insertParam);
    }

    public abstract List<Document> query(QueryParam param) throws VectorDBException;

    public abstract List<List<Document>> searchById(SearchByIdParam param) throws VectorDBException;

    public abstract AffectRes delete(DeleteParam param) throws VectorDBException;

    public abstract AffectRes update(UpdateParam param, Document document) throws VectorDBException;

    public BaseRes rebuildIndex(RebuildIndexParam rebuildIndexParam) {
        return this.stub.rebuildIndex(new RebuildIndexParamInner(this.database, this.collection, rebuildIndexParam));
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "Create collection param error: %s", e));
        }
    }


    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class IndexStatus {
        private String status;
        private Date startTime;

        public String getStatus() {
            return status;
        }

        public Date getStartTime() {
            return startTime;
        }

        @Override
        public String toString() {
            return "IndexStatus{" +
                    "status='" + status + '\'' +
                    ", startTime=" + startTime +
                    '}';
        }
    }
}
